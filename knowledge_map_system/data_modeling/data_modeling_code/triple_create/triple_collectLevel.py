import csv
import pandas as pd
output_file = 'C:\\Users\\15211\\Desktop\\数据\\triple\\triple_collectLevel.csv'
col_name = ['collectLevel','collectLevel']
input_file=['C:\\Users\\15211\\Desktop\\数据\\add_id\\3D_LiaoNing_museum.csv',
            'C:\\Users\\15211\\Desktop\\数据\\add_id\\LiaoNing_museum.csv']

with open(output_file, 'w', newline='', encoding='utf-8') as f_out:
    writer = csv.writer(f_out)
    writer.writerow(["collectID","relationship","collectLevel"])  # 写入表头
    for i in range(0, len(input_file)):
        with open(input_file[i], 'r', encoding='utf-8') as f_in:
            reader = csv.DictReader(f_in)
            for row in reader:
                writer.writerow([row["collectID"],"藏品等级",row[col_name[i]]])
            
print("finished!!!")