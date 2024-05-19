import csv
import pandas as pd
output_file = 'C:\\Users\\15211\\Desktop\\数据\\triple\\triple_museum.csv'
col_name = ['museum','museum','博物馆','museum','museum']
input_file=['C:\\Users\\15211\\Desktop\\数据\\add_id\\3D_LiaoNing_museum.csv',
            'C:\\Users\\15211\\Desktop\\数据\\add_id\\LiaoNing_museum.csv',
            'C:\\Users\\15211\\Desktop\\数据\\add_id\\guangdong_museum.csv',
           'C:\\Users\\15211\\Desktop\\数据\\add_id\\cernuschi_museum.csv',
            'C:\\Users\\15211\\Desktop\\数据\\add_id\\indiana_museum.csv']

with open(output_file, 'w', newline='', encoding='utf-8') as f_out:
    writer = csv.writer(f_out)
    writer.writerow(["collectID","relationship","museum"])  # 写入表头
    for i in range(0, len(input_file)):
        with open(input_file[i], 'r', encoding='utf-8') as f_in:
            reader = csv.DictReader(f_in)
            for row in reader:
                writer.writerow([row["collectID"],"所处博物馆",row[col_name[i]]])
            
print("finished!!!")