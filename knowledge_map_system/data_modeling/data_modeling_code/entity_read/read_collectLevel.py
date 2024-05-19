import csv
import pandas as pd
output_file = 'output_collectLevel.csv'
col_name = ['collectLevel']
input_file=['C:\\Users\\15211\\Desktop\\数据\\add_id\\LiaoNing_museum.csv']

with open(output_file, 'w', newline='', encoding='utf-8') as f_out:
    writer = csv.writer(f_out)
    writer.writerow(["collectLevel"])  # 写入表头
    for i in range(0, len(input_file)):
        with open(input_file[i], 'r', encoding='utf-8') as f_in:
            reader = csv.DictReader(f_in)
            for row in reader:
                writer.writerow([row[col_name[i]]])
            
print("finished!!!")

input_file1 = 'output_collectLevel.csv'
output_file1 = 'output_unique_collectLevel.csv'
df = pd.read_csv(input_file1)
unique_collectLevel = df['collectLevel'].unique()
unique_df = pd.DataFrame({'collectLevel': unique_collectLevel})
unique_df.to_csv(output_file1, index=False)
print("去重完成，并已将结果写入到", output_file1)