# GitHub Actions CI/CD Setup Guide

## Overview

This GitHub Actions workflow automatically:
- ✅ Tests your backend code
- ✅ Scans for security vulnerabilities
- ✅ Builds Docker image
- ✅ Pushes to GitHub Container Registry
- ✅ Deploys to Fly.io
- ✅ Posts deployment status to pull requests

## Setup Instructions

### Step 1: Get Your Fly.io Token

```bash
# Login to Fly.io
flyctl auth login

# Get your API token
flyctl auth token
# This prints your token - copy it
```

### Step 2: Add Secret to GitHub

1. Go to your repository: `https://github.com/nithyanev5-cmd/lms-catalogue`
2. Navigate to **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**
4. Name: `FLY_API_TOKEN`
5. Value: Paste your Fly.io token from Step 1
6. Click **Add secret**

### Step 3: Verify Setup

```bash
# Check GitHub secrets are set correctly
gh secret list -R nithyanev5-cmd/lms-catalogue
```

### Step 4: Trigger Deployment

**Option A: Automatic (on push to master)**
```bash
git checkout master
git merge feature/testCourse  # Merge your PR
git push origin master  # Workflow runs automatically
```

**Option B: Manual Trigger**
1. Go to GitHub repo
2. Click **Actions** tab
3. Select **Deploy to Fly.io** workflow
4. Click **Run workflow** → Select **master** branch → **Run**

### Step 5: Monitor Deployment

```bash
# Watch GitHub Actions run
gh run list -R nithyanev5-cmd/lms-catalogue

# Follow specific run
gh run watch <RUN_ID>

# View deployment logs
gh run view <RUN_ID> --log

# Or check in browser:
# https://github.com/nithyanev5-cmd/lms-catalogue/actions
```

---

## Workflow Jobs

### 1. Build and Deploy
- Checks out code
- Builds Docker image
- Pushes to GitHub Container Registry
- Deploys to Fly.io
- Posts status to pull requests

### 2. Test
- Runs Maven tests
- Uploads test results as artifacts
- Fails workflow if tests fail (prevents bad deploys)

### 3. Security Scan
- Scans dependencies for vulnerabilities
- Uses Trivy security scanner
- Reports to GitHub Security tab

---

## Useful Commands

```bash
# List all workflow runs
gh run list -R nithyanev5-cmd/lms-catalogue

# View logs for a specific run
gh run view <RUN_ID> --log

# Cancel a running workflow
gh run cancel <RUN_ID>

# Re-run a failed workflow
gh run rerun <RUN_ID>

# View workflow status
gh workflow list -R nithyanev5-cmd/lms-catalogue

# Check GitHub Actions quota
gh api /user/actions/oidc/customization/sub -q '.use_default'
```

---

## Customization

### Deploy on Every Push (Not Just Master)
Edit `.github/workflows/deploy.yml`:
```yaml
on:
  push:
    branches:
      - '**'  # Deploy on all branches
```

### Only Deploy from Master
```yaml
on:
  push:
    branches:
      - master
      - main
```

### Deploy on Release Tags
```yaml
on:
  push:
    branches:
      - master
    tags:
      - 'v*'  # Deploy when tags like v1.0.0 are pushed
```

### Add Slack Notifications
Add to workflow:
```yaml
- name: Notify Slack
  uses: slackapi/slack-github-action@v1
  with:
    webhook-url: ${{ secrets.SLACK_WEBHOOK }}
```

---

## Troubleshooting

### Deployment fails with "FLY_API_TOKEN not found"
**Solution:** Make sure you added the secret to GitHub (Step 2)
```bash
gh secret list  # Verify it's there
```

### Tests fail and deployment doesn't happen
**Solution:** This is intentional - fix failing tests first
```bash
cd backend
mvn test  # Run tests locally
# Fix any failures, then push again
```

### Build times out
**Solution:** Increase timeout in workflow (workflow runs have 6-hour max)
Edit `.github/workflows/deploy.yml`:
```yaml
timeout-minutes: 30
```

### Docker image push fails
**Solution:** GitHub token permissions might be restricted
Go to **Settings** → **Actions** → **General** → Scroll to "Workflow permissions" → Select "Read and write permissions"

### Fly.io deployment fails
```bash
# Check Fly.io app status
flyctl status

# View recent deploys
flyctl releases list

# Check logs
flyctl logs --follow
```

---

## Cost

**GitHub Actions:**
- ✅ Free tier: 2,000 minutes/month (more than enough!)
- Private repo: Same free tier

**Fly.io:**
- ✅ Free tier: Included in free credits
- Estimated cost: Free

**Total Cost:** $0/month (completely free!)

---

## Monitoring Deployments

### Check from GitHub
```bash
# List all deployments
gh run list -R nithyanev5-cmd/lms-catalogue

# View specific run
gh run view <RUN_ID>
```

### Check from Fly.io
```bash
# View app status
flyctl status

# View logs
flyctl logs

# View recent deployments
flyctl releases list
```

### Check from Browser
- GitHub Actions: https://github.com/nithyanev5-cmd/lms-catalogue/actions
- Fly.io Dashboard: https://fly.io/dashboard/
- Live App: https://lms-catalogue.fly.dev

---

## Next Steps

1. **Merge feature branch to master**
   ```bash
   git checkout master
   git merge feature/testCourse
   git push origin master
   ```

2. **Workflow runs automatically** → Check Actions tab

3. **App deploys to Fly.io** → Live at https://lms-catalogue.fly.dev

4. **Monitor logs**
   ```bash
   flyctl logs --follow
   ```

---

## Workflow Status Badge

Add to README.md:
```markdown
![Deploy to Fly.io](https://github.com/nithyanev5-cmd/lms-catalogue/actions/workflows/deploy.yml/badge.svg)
```

---

**Your CI/CD pipeline is now set up! 🚀**

Every push to `master` will automatically:
1. Run tests
2. Scan for security issues
3. Build and push Docker image
4. Deploy to Fly.io
5. Post status to GitHub
