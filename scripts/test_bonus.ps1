# 加分项勾选功能 - API 自测脚本

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "加分项勾选功能 - 完整 API 自测" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# 测试数据
$testCases = @(
    @{id=1; name="临床检验中心"; expectDir=1; expectSec=1},
    @{id=2; name="护理质控中心"; expectDir=1; expectSec=0},
    @{id=3; name="省防盲指导中心"; expectDir=0; expectSec=1},
    @{id=4; name="省骨科技术指导中心"; expectDir=0; expectSec=0}
)

Write-Host "`n【步骤 1】查询各机构的加分状态（验证数据是否正确保存）`n" -ForegroundColor Yellow

$allPass = $true

foreach ($case in $testCases) {
    Write-Host "$($case.name) (ID=$($case.id)):" -ForegroundColor Cyan
    Write-Host "  预期：主任演讲=$($case.expectDir), 秘书参与=$($case.expectSec)" -ForegroundColor Gray
    
    try {
        $r = Invoke-WebRequest -Uri "http://localhost:5031/api/bonus?taskId=1&institutionId=$($case.id)" -Method GET -UseBasicParsing
        $j = $r.Content | ConvertFrom-Json
        
        $actualDir = $j.data.directorPresentation
        $actualSec = $j.data.secretaryParticipation
        
        Write-Host "  实际：主任演讲=$actualDir, 秘书参与=$actualSec" -ForegroundColor Gray
        
        $dirPass = $actualDir -eq $case.expectDir
        $secPass = $actualSec -eq $case.expectSec
        
        if ($dirPass -and $secPass) {
            Write-Host "  结果：✅ PASS" -ForegroundColor Green
        } else {
            Write-Host "  结果：❌ FAIL" -ForegroundColor Red
            $allPass = $false
        }
    } catch {
        Write-Host "  错误：$_" -ForegroundColor Red
        $allPass = $false
    }
    
    Write-Host ""
}

Write-Host "========================================" -ForegroundColor Cyan
if ($allPass) {
    Write-Host "🎉 所有测试通过！数据完全符合预期！" -ForegroundColor Green
} else {
    Write-Host "❌ 有测试失败，请检查！" -ForegroundColor Red
}
Write-Host "========================================`n" -ForegroundColor Cyan
