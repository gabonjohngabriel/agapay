Write-Host "===============================================================================" -ForegroundColor Cyan
Write-Host "  AGAPAY CAMPUS HELP DESK - EXECUTING AUTOMATED VERIFICATION SUITE" -ForegroundColor Cyan
Write-Host "===============================================================================" -ForegroundColor Cyan
Write-Host ""

$javaBin = "C:\Program Files\Java\jdk-26.0.1\bin\java.exe"
if (-not (Test-Path $javaBin)) {
    $javaBin = "java"
}

& $javaBin -cp "target/classes;target/test-classes" bsit2e_g1.VerificationSuite

Write-Host ""
Write-Host "Verification completed. See VERIFICATION_REPORT.md for full breakdown." -ForegroundColor Green
