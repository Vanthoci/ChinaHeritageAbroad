import csv
import pandas as pd
output_file = 'C:\\Users\\15211\\Desktop\\数据\\triple\\triple_source.csv'
col_name = ['source']
input_file=['C:\\Users\\15211\\Desktop\\数据\\add_id\\LiaoNing_museum.csv']

with open(output_file, 'w', newline='', encoding='utf-8') as f_out:
    writer = csv.writer(f_out)
    writer.writerow(["collectID","relationship","source"])  # 写入表头
    for i in range(0, len(input_file)):
        with open(input_file[i], 'r', encoding='utf-8') as f_in:
            reader = csv.DictReader(f_in)
            for row in reader:
                writer.writerow([row["collectID"],"藏品来源",row[col_name[i]]])
            
print("finished!!!")