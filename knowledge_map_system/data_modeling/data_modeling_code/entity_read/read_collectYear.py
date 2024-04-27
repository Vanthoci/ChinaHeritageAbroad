import csv
import pandas as pd
output_file = 'C:\\Users\\15211\\Desktop\\数据\\output_collectYear.csv'
col_name = ['collectDetailYear','relicTime','年代','relicTime','collectYear']
#col_name = ['年代']
input_file=['C:\\Users\\15211\\Desktop\\数据\\add_id\\3D_LiaoNing_museum.csv',
            'C:\\Users\\15211\\Desktop\\数据\\add_id\\cernuschi_museum.csv',
            'C:\\Users\\15211\\Desktop\\数据\\add_id\\guangdong_museum.csv',
            'C:\\Users\\15211\\Desktop\\数据\\add_id\\indiana_museum.csv',
            'C:\\Users\\15211\\Desktop\\数据\\add_id\\LiaoNing_museum.csv']
#input_file=['C:\\Users\\15211\\Desktop\\数据\\add_id\\广东省数据.csv',]

with open(output_file, 'w', newline='', encoding='utf-8') as f_out:
    writer = csv.writer(f_out)
    writer.writerow(["collectYear","Year_Introduction"])  # 写入表头
    for i in range(0, len(input_file)):
        with open(input_file[i], 'r', encoding='utf-8') as f_in:
            reader = csv.DictReader(f_in)
            for row in reader:
                if i==2:
                    writer.writerow([row[col_name[i]],row["年代备注"]])
                else:
                    writer.writerow([row[col_name[i]],""])
print("finished!!!")

input_file1 = 'C:\\Users\\15211\\Desktop\\数据\\output_collectYear.csv'
output_file1 = 'C:\\Users\\15211\\Desktop\\数据\\output_unique_collectYear.csv'
df = pd.read_csv(input_file1)
unique_collectYear = df['collectYear'].unique()
unique_df = pd.DataFrame({'collectYear': unique_collectYear})
unique_df.to_csv(output_file1, index=False)
print("去重完成，并已将结果写入到", output_file1)