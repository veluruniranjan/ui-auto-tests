terraform {
  backend "s3" {
    dynamodb_table = "terraform_state_lock"
    profile = "terraform"
  }
}

provider "aws" {
  region = var.aws_region
  max_retries = var.max_retries
  allowed_account_ids = [module.global_variables.aws_account_id[var.aws_account_name]]
  profile = "terraform"
}

module "global_variables" {
  source = "git@github.com:zenjob/infra-code.git//terraform/basic_modules/global_variables"
}

module "secrets" {
  source = "git@github.com:zenjob/infra-code.git//terraform/modules/secret_setup/v3"

  k8s_namespace = var.namespace
  service_name = var.service_name

  subkeys = {
    "opsDashboardPassword" = var.opsDashboardPassword
    "test_users_password" = var.test_users_password
  }
}
