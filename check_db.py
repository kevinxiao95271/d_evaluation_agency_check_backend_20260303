import mysql.connector
import json

print("\n========================================")
print("检查 MySQL 数据库数据")
print("========================================\n")

try:
    # 连接数据库
    conn = mysql.connector.connect(
        host="localhost",
        user="root",
        password="123456",
        database="evaluation_agency_check"
    )
    
    cursor = conn.cursor(dictionary=True)
    
    # 查询附加项数据
    sql = """
        SELECT id, task_id, institution_id, director_presentation, 
               secretary_participation, filled_by, filled_at, 
               create_time, update_time 
        FROM institution_bonus 
        WHERE task_id=1 AND institution_id=1
        ORDER BY id DESC 
        LIMIT 1
    """
    
    cursor.execute(sql)
    result = cursor.fetchone()
    
    if result:
        print("✅ 数据库中找到数据:")
        print(f"  ID: {result['id']}")
        print(f"  Task ID: {result['task_id']}")
        print(f"  Institution ID: {result['institution_id']}")
        print(f"  主任演讲：{result['director_presentation']} ✅")
        print(f"  秘书参与：{result['secretary_participation']} ✅")
        print(f"  操作人：{result['filled_by']}")
        print(f"  操作时间：{result['filled_at']}")
        print(f"  创建时间：{result['create_time']}")
        print(f"  更新时间：{result['update_time']}")
        
        # 验证数据是否正确
        if result['director_presentation'] == 1 and result['secretary_participation'] == 1:
            print("\n✅ 数据验证通过！两个字段都正确保存为 1")
        else:
            print("\n❌ 数据验证失败！期望值都是 1")
    else:
        print("❌ 数据库中没有找到数据")
    
    cursor.close()
    conn.close()
    
except Exception as e:
    print(f"❌ 数据库连接错误：{e}")

print("\n========================================\n")
