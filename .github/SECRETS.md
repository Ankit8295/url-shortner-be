# GitHub Actions setup for ECS deploy
#
# 1. Repo Settings → Secrets and variables → Actions → New repository secret:
#    - AWS_ACCESS_KEY_ID     (IAM user url-shortner-deploy access key)
#    - AWS_SECRET_ACCESS_KEY
#
# 2. Confirm IAM user can:
#    - ecr:* on this repo (or AmazonEC2ContainerRegistryPowerUser)
#    - ecs:UpdateService, ecs:DescribeServices
#    - iam:PassRole if needed for task execution
#    Or attach AdministratorAccess for learning.
#
# 3. If your ECS service name differs, edit ECS_SERVICE in
#    .github/workflows/ci-deploy.yml
#
# 4. Push to main → Actions tab → watch deploy.
