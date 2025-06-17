#!/bin/bash

echo "🚀 Starting Customer Analytics Dashboard Application"
echo "=================================================="

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker first."
    exit 1
fi

echo "📦 Starting services with Docker Compose..."
docker-compose up -d

echo "⏳ Waiting for services to start..."
sleep 10

# Check if services are running
echo "🔍 Checking service status..."
docker-compose ps

echo ""
echo "✅ Application is starting up!"
echo ""
echo "🌐 Access Points:"
echo "   Frontend: http://localhost:3000"
echo "   Backend API: http://localhost:8088"
echo "   Analytics Dashboard: http://localhost:3000/dashboard/analytics"
echo ""
echo "📊 New Features Added:"
echo "   ✅ Customer Search & Filtering"
echo "   ✅ Customer Analytics Dashboard"
echo "   ✅ Real-time Statistics"
echo "   ✅ Interactive Charts"
echo ""
echo "🔧 To run frontend locally (for development):"
echo "   cd frontend/react"
echo "   npm install"
echo "   npm run dev"
echo ""
echo "📝 API Endpoints:"
echo "   GET  /api/v1/customers/analytics - Get customer analytics"
echo "   POST /api/v1/customers/search - Search customers with filters"
echo "   GET  /api/v1/customers - Get all customers"
echo ""
echo "🎯 To test the analytics feature:"
echo "   1. Go to http://localhost:3000"
echo "   2. Login or sign up"
echo "   3. Click 'Analytics' in the sidebar"
echo "   4. View your customer statistics and charts!"
echo ""
echo "🔄 To stop the application:"
echo "   docker-compose down" 