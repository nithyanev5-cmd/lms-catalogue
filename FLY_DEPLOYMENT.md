# Fly.io Deployment Guide

## Prerequisites

1. **Create a free Fly.io account**: https://fly.io/app/sign-up
2. **Install Flyctl CLI**: https://fly.io/docs/getting-started/installing-flyctl/

### Install on Linux/Mac:
```bash
curl -L https://fly.io/install.sh | sh
```

### Install on Windows (PowerShell):
```powershell
iwr https://fly.io/install.ps1 -useb | iex
```

## Quick Deployment Steps

### 1. Login to Fly.io
```bash
flyctl auth login
# Or create account: flyctl auth signup
```

### 2. Deploy Backend

```bash
# Navigate to project root
cd /path/to/lms-catalogue

# Create Fly app (first time only)
flyctl launch

# When prompted:
# - App name: lms-catalogue (or your choice)
# - Region: sjc (San Jose) or your preferred region
# - Postgres database: Yes (optional, for production)
# - Redis cache: No
# - Would you like to set secrets: Yes (if using sensitive env vars)

# Deploy
flyctl deploy
```

### 3. Set Environment Variables

```bash
# Set database connection (if using managed database)
flyctl secrets set SPRING_DATASOURCE_URL="jdbc:mysql://..." \
  SPRING_DATASOURCE_USERNAME="lms_app" \
  SPRING_DATASOURCE_PASSWORD="your_password"

# Or use the dashboard: https://fly.io/dashboard/
```

### 4. Monitor Deployment

```bash
# View deployment status
flyctl status

# View logs
flyctl logs

# View app details
flyctl info

# SSH into running app (if needed)
flyctl ssh console
```

## Full Stack Deployment (Multiple Services)

For deploying frontend + backend + database separately:

### Option A: Deploy Backend Only (Recommended for Free Tier)

The free tier can run one app. Deploy the backend API:

```bash
flyctl deploy
# Backend runs at: https://lms-catalogue.fly.dev
```

### Option B: Deploy Frontend Separately

Create a second app for the frontend:

```bash
cd frontend

# Create frontend app
flyctl launch --generate-name

# Update vite.config.ts to point to backend API:
# VITE_API_URL=https://lms-catalogue.fly.dev

# Deploy
flyctl deploy
```

### Option C: Deploy Full Stack (Requires Paid Tier)

Use docker-compose or separate Fly apps for each service.

## Database Setup

### Option 1: Use Fly.io Managed Postgres (Recommended)
```bash
flyctl postgres create
# Automatic setup with connection string
```

### Option 2: Use External MySQL Service
```bash
# Set connection string via secrets
flyctl secrets set SPRING_DATASOURCE_URL="jdbc:mysql://your-hosted-db:3306/lms_catalog"
```

### Option 3: Run MySQL in Fly.io (Advanced)
Create separate `fly.toml` for database app (requires more resources).

## Scaling

```bash
# Scale instances
flyctl scale count 2

# View current scaling
flyctl scale show

# Scale resources (CPU/Memory)
flyctl scale vm shared-cpu-1x
flyctl scale memory 512
```

## Useful Commands

```bash
# View all apps
flyctl apps list

# Check app status
flyctl status

# Real-time logs
flyctl logs --follow

# Deploy new version
flyctl deploy

# Rollback to previous version
flyctl releases list
flyctl releases rollback

# Remove app
flyctl apps destroy lms-catalogue

# View metrics
flyctl metrics

# SSH into app
flyctl ssh console

# Execute command in app
flyctl ssh console -- curl http://localhost:8080/actuator/health
```

## Troubleshooting

### App won't start
```bash
# Check logs
flyctl logs --follow

# Look for common issues:
# 1. Database connection failed
# 2. Port not exposed correctly
# 3. Memory/CPU limits exceeded
```

### Database connection issues
```bash
# Verify environment variables
flyctl secrets list

# Test connection from app
flyctl ssh console
# Inside container:
wget http://localhost:8080/actuator/health
```

### Build failures
```bash
# Rebuild without cache
flyctl deploy --build-arg SKIP_TESTS=true

# View build logs
flyctl logs --follow
```

## Cost Estimates

### Free Tier Includes:
- 3 shared-cpu-1x 256MB apps per organization
- 3 Postgres databases (free up to 3)
- 160GB bandwidth/month
- $5 credit/month

### After Free Tier:
- Shared CPU 1x: $2.97/month
- 1GB RAM: $4.50/month
- Additional bandwidth: $0.02/GB

**Estimated cost for basic setup**: Free to $10/month

## Next Steps After Deployment

1. **Monitor your app**: https://fly.io/dashboard/
2. **Set up custom domain** (if you have one)
3. **Configure SSL certificates** (automatic with Fly.io)
4. **Set up monitoring and alerts**
5. **Enable database backups**

## Links

- Fly.io Dashboard: https://fly.io/dashboard/
- Fly.io Docs: https://fly.io/docs/
- Pricing: https://fly.io/docs/about/pricing/
- Reference: https://fly.io/docs/reference/flyctl/

## Your Deployed App URL

After deployment, your app will be available at:
```
https://lms-catalogue.fly.dev
```

(Replace `lms-catalogue` with your actual app name)
