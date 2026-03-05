import requests
import json

print("\n========================================")
print("加分项勾选功能 - API 自测")
print("========================================\n")

# 测试数据
test_cases = [
    {"id": 1, "name": "临床检验中心", "expect_dir": 1, "expect_sec": 1},
    {"id": 2, "name": "护理质控中心", "expect_dir": 1, "expect_sec": 0},
    {"id": 3, "name": "省防盲指导中心", "expect_dir": 0, "expect_sec": 1},
    {"id": 4, "name": "省骨科技术指导中心", "expect_dir": 0, "expect_sec": 0}
]

all_pass = True

for case in test_cases:
    print(f"{case['name']} (ID={case['id']}):")
    print(f"  预期：主任演讲={case['expect_dir']}, 秘书参与={case['expect_sec']}")
    
    try:
        response = requests.get(f"http://localhost:5031/api/bonus?taskId=1&institutionId={case['id']}")
        data = response.json()
        
        actual_dir = data['data']['directorPresentation']
        actual_sec = data['data']['secretaryParticipation']
        
        print(f"  实际：主任演讲={actual_dir}, 秘书参与={actual_sec}")
        
        dir_pass = actual_dir == case['expect_dir']
        sec_pass = actual_sec == case['expect_sec']
        
        if dir_pass and sec_pass:
            print(f"  结果：✅ PASS")
        else:
            print(f"  结果：❌ FAIL")
            all_pass = False
    except Exception as e:
        print(f"  错误：{e}")
        all_pass = False
    
    print("")

print("========================================")
if all_pass:
    print("🎉 所有测试通过！数据完全符合预期！")
else:
    print("❌ 有测试失败，请检查！")
print("========================================\n")
