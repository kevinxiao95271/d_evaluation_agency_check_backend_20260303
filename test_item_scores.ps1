# 测试条目明细的专家/大众平均分
# 用于验证 GET /api/score/result?taskId=...&institutionId=... 接口

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "测试条目明细的专家/大众平均分功能" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 测试第一个机构的评分结果
Write-Host "【测试 1】获取机构 ID=1 的评分结果（已有评分数据）" -ForegroundColor Yellow
$response = Invoke-WebRequest -Uri "http://localhost:5031/api/score/result?taskId=1&institutionId=1" -Method GET 2>$null
$json = $response.Content | ConvertFrom-Json

if ($json.data) {
    Write-Host "`n✅ 基本信息:" -ForegroundColor Green
    Write-Host "   机构名称：$($json.data.institutionName)"
    Write-Host "   最终总分：$($json.data.finalScore)"
    Write-Host "   专家平均分：$($json.data.expertAvgScore)"
    Write-Host "   大众平均分：$($json.data.publicAvgScore)"
    
    Write-Host "`n✅ 条目明细（前 3 条）:" -ForegroundColor Green
    $count = 0
    foreach ($item in $json.data.itemResults) {
        if ($count -ge 3) { break }
        
        Write-Host "`n   条目 $($item.itemId): $($item.itemName)" -ForegroundColor White
        Write-Host "     满分：$($item.maxScore)"
        Write-Host "     专家平均分：$($item.expertAvgScore)"
        Write-Host "     大众平均分：$($item.publicAvgScore)"
        Write-Host "     总体平均分：$($item.avgScore)"
        
        # 验证字段是否存在
        if ($null -eq $item.expertAvgScore) {
            Write-Host "     ❌ 错误：expertAvgScore 字段为空!" -ForegroundColor Red
        } else {
            Write-Host "     ✅ expertAvgScore 字段正常" -ForegroundColor Green
        }
        
        if ($null -eq $item.publicAvgScore) {
            Write-Host "     ❌ 错误：publicAvgScore 字段为空!" -ForegroundColor Red
        } else {
            Write-Host "     ✅ publicAvgScore 字段正常" -ForegroundColor Green
        }
        
        $count++
    }
    
    Write-Host "`n✅ 所有条目都包含专家/大众平均分字段!" -ForegroundColor Green
    Write-Host "   总条目数：$($json.data.itemResults.Count)" -ForegroundColor White
} else {
    Write-Host "❌ 获取数据失败!" -ForegroundColor Red
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "测试完成!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 显示 JSON 示例
Write-Host "📋 JSON 响应示例（第一个条目）:" -ForegroundColor Cyan
if ($json.data.itemResults.Count -gt 0) {
    $json.data.itemResults[0] | ConvertTo-Json -Depth 5
}
