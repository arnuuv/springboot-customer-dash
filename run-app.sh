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
echo "   Export/Import: http://localhost:3000/dashboard/export-import"
echo "   Activity Timeline: http://localhost:3000/dashboard/activities"
echo ""
echo "📊 Features Available:"
echo "   ✅ Customer Search & Filtering"
echo "   ✅ Customer Analytics Dashboard"
echo "   ✅ Customer Export/Import (CSV)"
echo "   ✅ Customer Activity Logging"
echo "   ✅ Real-time Statistics"
echo "   ✅ Interactive Charts"
echo "   ✅ Activity Timeline"
echo ""
echo "🔧 To run frontend locally (for development):"
echo "   cd frontend/react"
echo "   npm install"
echo "   npm run dev"
echo ""
echo "📝 API Endpoints:"
echo "   GET  /api/v1/customers/analytics - Get customer analytics"
echo "   POST /api/v1/customers/search - Search customers with filters"
echo "   GET  /api/v1/customers/export/csv - Export customers to CSV"
echo "   POST /api/v1/customers/import/csv - Import customers from CSV"
echo "   GET  /api/v1/customers/activities - Get all activities"
echo "   GET  /api/v1/customers/activities/recent - Get recent activities"
echo "   GET  /api/v1/customers - Get all customers"
echo ""
echo "🎯 How to test the new features:"
echo "   1. Go to http://localhost:3000"
echo "   2. Login or sign up"
echo "   3. Navigate using the sidebar:"
echo "      📊 Analytics - View customer statistics and charts"
echo "      📥 Export/Import - Export/import customer data"
echo "      📈 Activities - View customer activity timeline"
echo ""
echo "🔄 To stop the application:"
echo "   docker-compose down" 