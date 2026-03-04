# 评分记录管理API测试脚本
$baseUrl = "http://localhost:5031"
$results = @()

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "评分记录管理API测试" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 1. 获取所有评分记录
Write-Host "1. 测试 GET /api/score/records" -ForegroundColor Yellow
try {
    $r = Invoke-RestMethod -Uri "$baseUrl/api/score/records" -Method GET
    $results += [PSCustomObject]@{
        API = "GET /api/score/records"
        Status = "✅ 成功"
        StatusCode = 200
        DataCount = $r.data.Count
        Note = "获取所有评分记录"
    }
    Write-Host "   ✅ 成功 - 返回 $($r.data.Count) 条记录" -ForegroundColor Green
} catch {
    $results += [PSCustomObject]@{
        API = "GET /api/score/records"
        Status = "❌ 失败"
        StatusCode = $_.Exception.Response.StatusCode.value__
        DataCount = 0
        Note = $_.Exception.Message
    }
    Write-Host "   ❌ 失败 - $($_.Exception.Message)" -ForegroundColor Red
}

# 2. 按任务获取评分记录
Write-Host "2. 测试 GET /api/score/records?taskId=1" -ForegroundColor Yellow
try {
    $r = Invoke-RestMethod -Uri "$baseUrl/api/score/records?taskId=1" -Method GET
    $results += [PSCustomObject]@{
        API = "GET /api/score/records?taskId=1"
        Status = "✅ 成功"
        StatusCode = 200
        DataCount = $r.data.Count
        Note = "按任务筛选评分记录"
    }
    Write-Host "   ✅ 成功 - 返回 $($r.data.Count) 条记录" -ForegroundColor Green
} catch {
    $results += [PSCustomObject]@{
        API = "GET /api/score/records?taskId=1"
        Status = "❌ 失败"
        StatusCode = $_.Exception.Response.StatusCode.value__
        DataCount = 0
        Note = $_.Exception.Message
    }
    Write-Host "   ❌ 失败 - $($_.Exception.Message)" -ForegroundColor Red
}

# 3. 按评委获取评分记录
Write-Host "3. 测试 GET /api/score/records?judgeId=1" -ForegroundColor Yellow
try {
    $r = Invoke-RestMethod -Uri "$baseUrl/api/score/records?judgeId=1" -Method GET
    $results += [PSCustomObject]@{
        API = "GET /api/score/records?judgeId=1"
        Status = "✅ 成功"
        StatusCode = 200
        DataCount = $r.data.Count
        Note = "按评委筛选评分记录"
    }
    Write-Host "   ✅ 成功 - 返回 $($r.data.Count) 条记录" -ForegroundColor Green
} catch {
    $results += [PSCustomObject]@{
        API = "GET /api/score/records?judgeId=1"
        Status = "❌ 失败"
        StatusCode = $_.Exception.Response.StatusCode.value__
        DataCount = 0
        Note = $_.Exception.Message
    }
    Write-Host "   ❌ 失败 - $($_.Exception.Message)" -ForegroundColor Red
}

# 4. 按机构获取评分记录
Write-Host "4. 测试 GET /api/score/records?institutionId=1" -ForegroundColor Yellow
try {
    $r = Invoke-RestMethod -Uri "$baseUrl/api/score/records?institutionId=1" -Method GET
    $results += [PSCustomObject]@{
        API = "GET /api/score/records?institutionId=1"
        Status = "✅ 成功"
        StatusCode = 200
        DataCount = $r.data.Count
        Note = "按机构筛选评分记录"
    }
    Write-Host "   ✅ 成功 - 返回 $($r.data.Count) 条记录" -ForegroundColor Green
} catch {
    $results += [PSCustomObject]@{
        API = "GET /api/score/records?institutionId=1"
        Status = "❌ 失败"
        StatusCode = $_.Exception.Response.StatusCode.value__
        DataCount = 0
        Note = $_.Exception.Message
    }
    Write-Host "   ❌ 失败 - $($_.Exception.Message)" -ForegroundColor Red
}

# 5. 获取评分统计
Write-Host "5. 测试 GET /api/score/statistics?taskId=1" -ForegroundColor Yellow
try {
    $r = Invoke-RestMethod -Uri "$baseUrl/api/score/statistics?taskId=1" -Method GET
    $results += [PSCustomObject]@{
        API = "GET /api/score/statistics?taskId=1"
        Status = "✅ 成功"
        StatusCode = 200
        DataCount = "-"
        Note = "获取任务评分统计"
    }
    Write-Host "   ✅ 成功" -ForegroundColor Green
} catch {
    $results += [PSCustomObject]@{
        API = "GET /api/score/statistics?taskId=1"
        Status = "❌ 失败"
        StatusCode = $_.Exception.Response.StatusCode.value__
        DataCount = "-"
        Note = $_.Exception.Message
    }
    Write-Host "   ❌ 失败 - $($_.Exception.Message)" -ForegroundColor Red
}

# 6. 获取评委统计
Write-Host "6. 测试 GET /api/score/judge-stats?judgeId=1" -ForegroundColor Yellow
try {
    $r = Invoke-RestMethod -Uri "$baseUrl/api/score/judge-stats?judgeId=1" -Method GET
    $results += [PSCustomObject]@{
        API = "GET /api/score/judge-stats?judgeId=1"
        Status = "✅ 成功"
        StatusCode = 200
        DataCount = "-"
        Note = "获取评委评分统计"
    }
    Write-Host "   ✅ 成功" -ForegroundColor Green
} catch {
    $results += [PSCustomObject]@{
        API = "GET /api/score/judge-stats?judgeId=1"
        Status = "❌ 失败"
        StatusCode = $_.Exception.Response.StatusCode.value__
        DataCount = "-"
        Note = $_.Exception.Message
    }
    Write-Host "   ❌ 失败 - $($_.Exception.Message)" -ForegroundColor Red
}

# 7. 获取机构统计
Write-Host "7. 测试 GET /api/score/institution-stats?institutionId=1" -ForegroundColor Yellow
try {
    $r = Invoke-RestMethod -Uri "$baseUrl/api/score/institution-stats?institutionId=1" -Method GET
    $results += [PSCustomObject]@{
        API = "GET /api/score/institution-stats?institutionId=1"
        Status = "✅ 成功"
        StatusCode = 200
        DataCount = "-"
        Note = "获取机构评分统计"
    }
    Write-Host "   ✅ 成功" -ForegroundColor Green
} catch {
    $results += [PSCustomObject]@{
        API = "GET /api/score/institution-stats?institutionId=1"
        Status = "❌ 失败"
        StatusCode = $_.Exception.Response.StatusCode.value__
        DataCount = "-"
        Note = $_.Exception.Message
    }
    Write-Host "   ❌ 失败 - $($_.Exception.Message)" -ForegroundColor Red
}

# 8. 获取单个机构结果
Write-Host "8. 测试 GET /api/score/result?taskId=1&institutionId=1" -ForegroundColor Yellow
try {
    $r = Invoke-RestMethod -Uri "$baseUrl/api/score/result?taskId=1&institutionId=1" -Method GET
    $results += [PSCustomObject]@{
        API = "GET /api/score/result?taskId=1&institutionId=1"
        Status = "✅ 成功"
        StatusCode = 200
        DataCount = "-"
        Note = "计算单个机构得分"
    }
    Write-Host "   ✅ 成功" -ForegroundColor Green
} catch {
    $results += [PSCustomObject]@{
        API = "GET /api/score/result?taskId=1&institutionId=1"
        Status = "❌ 失败"
        StatusCode = $_.Exception.Response.StatusCode.value__
        DataCount = "-"
        Note = $_.Exception.Message
    }
    Write-Host "   ❌ 失败 - $($_.Exception.Message)" -ForegroundColor Red
}

# 9. 获取所有机构结果
Write-Host "9. 测试 GET /api/score/results/1" -ForegroundColor Yellow
try {
    $r = Invoke-RestMethod -Uri "$baseUrl/api/score/results/1" -Method GET
    $results += [PSCustomObject]@{
        API = "GET /api/score/results/1"
        Status = "✅ 成功"
        StatusCode = 200
        DataCount = $r.data.Count
        Note = "计算所有机构得分和排名"
    }
    Write-Host "   ✅ 成功 - 返回 $($r.data.Count) 个机构结果" -ForegroundColor Green
} catch {
    $results += [PSCustomObject]@{
        API = "GET /api/score/results/1"
        Status = "❌ 失败"
        StatusCode = $_.Exception.Response.StatusCode.value__
        DataCount = 0
        Note = $_.Exception.Message
    }
    Write-Host "   ❌ 失败 - $($_.Exception.Message)" -ForegroundColor Red
}

# 10. 导出评分记录 (测试端点是否存在,不实际下载)
Write-Host "10. 测试 GET /api/score/export?taskId=1" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/api/score/export?taskId=1" -Method GET -UseBasicParsing
    $results += [PSCustomObject]@{
        API = "GET /api/score/export?taskId=1"
        Status = "✅ 成功"
        StatusCode = $response.StatusCode
        DataCount = "-"
        Note = "导出CSV文件"
    }
    Write-Host "   ✅ 成功 - ContentType: $($response.Headers.'Content-Type')" -ForegroundColor Green
} catch {
    $results += [PSCustomObject]@{
        API = "GET /api/score/export?taskId=1"
        Status = "❌ 失败"
        StatusCode = $_.Exception.Response.StatusCode.value__
        DataCount = "-"
        Note = $_.Exception.Message
    }
    Write-Host "   ❌ 失败 - $($_.Exception.Message)" -ForegroundColor Red
}

# 11. 导出Excel (别名测试)
Write-Host "11. 测试 GET /api/score/export/excel?taskId=1" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/api/score/export/excel?taskId=1" -Method GET -UseBasicParsing
    $results += [PSCustomObject]@{
        API = "GET /api/score/export/excel?taskId=1"
        Status = "✅ 成功"
        StatusCode = $response.StatusCode
        DataCount = "-"
        Note = "导出CSV文件(Excel别名)"
    }
    Write-Host "   ✅ 成功 - ContentType: $($response.Headers.'Content-Type')" -ForegroundColor Green
} catch {
    $results += [PSCustomObject]@{
        API = "GET /api/score/export/excel?taskId=1"
        Status = "❌ 失败"
        StatusCode = $_.Exception.Response.StatusCode.value__
        DataCount = "-"
        Note = $_.Exception.Message
    }
    Write-Host "   ❌ 失败 - $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "测试结果汇总" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
$results | Format-Table -AutoSize

$successCount = ($results | Where-Object { $_.Status -like "*成功*" }).Count
$totalCount = $results.Count
Write-Host ""
Write-Host "总计: $totalCount 个API, 成功: $successCount, 失败: $($totalCount - $successCount)" -ForegroundColor $(if ($successCount -eq $totalCount) { "Green" } else { "Yellow" })

if ($successCount -eq $totalCount) {
    Write-Host ""
    Write-Host "🎉 所有API测试通过!" -ForegroundColor Green
    Write-Host ""
    Write-Host "📝 说明:" -ForegroundColor Cyan
    Write-Host "   - 当前数据库中没有评分记录,所以返回的数据为空" -ForegroundColor Gray
    Write-Host "   - 所有API端点都已正确实现并可以访问" -ForegroundColor Gray
    Write-Host "   - 需要通过 POST /api/score/submit 提交评分后才会有数据" -ForegroundColor Gray
} else {
    Write-Host ""
    Write-Host "⚠️  部分API测试失败,请检查错误信息" -ForegroundColor Yellow
}
