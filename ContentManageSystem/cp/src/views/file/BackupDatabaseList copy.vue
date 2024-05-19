<template>
  <div>
    <el-button type="primary" @click="fetchBackups">刷新备份列表</el-button>
    <el-button type="success" @click="createBackup">创建备份</el-button>
    <el-table :data="backups" style="width: 100%" stripe>
      <el-table-column prop="id" label="备份ID" width="180"></el-table-column>
      <el-table-column prop="name" label="备份名称" width="280"></el-table-column>
      <el-table-column prop="date" label="备份时间" width="180"></el-table-column>
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button size="mini" @click="restoreBackup(row.name)">恢复</el-button>
          <el-button size="mini" type="danger" @click="deleteBackup(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

const backups = ref([]);

// const fetchBackups = async () => {
//   try {
//     // 模拟请求延时
//     await new Promise(resolve => setTimeout(resolve, 1000));

//     // 使用静态数据替代API调用
//     backups.value = [
//       { id: 1, name: '备份一', date: '2022-01-01 12:00' },
//       { id: 2, name: '备份二', date: '2022-02-01 13:00' },
//       { id: 3, name: '备份三', date: '2022-03-01 14:00' }
//     ];
//     ElMessage.success('备份列表加载成功');
//   } catch (error) {
//     ElMessage.error('获取备份列表失败: ' + error.message);
//   }
// };

const fetchBackups = async () => {
  try {
    const response = await axios.get('/backend/ext/backup-database');
    backups.value = response.data;
  } catch (error) {
    ElMessage.error('获取备份列表失败: ' + error.message);
  }
};

const createBackup = async () => {
  try {
    await axios.post('/backend/ext/backup-database');
    ElMessage.success('备份已创建');
    fetchBackups();
  } catch (error) {
    ElMessage.error('创建备份失败: ' + error.message);
  }
};

const restoreBackup = async (name) => {
  try {
    await axios.post('/backend/ext/backup-database?_method=put', { name });
    ElMessage.success('备份已恢复');
  } catch (error) {
    ElMessage.error('恢复备份失败: ' + error.message);
  }
};

const deleteBackup = async (backup) => {
  try {
    await axios.post('/backend/ext/backup-database?_method=delete', [backup.id]);
    ElMessage.success('备份已删除');
    fetchBackups();
  } catch (error) {
    ElMessage.error('删除备份失败: ' + error.message);
  }
};

onMounted(fetchBackups);
</script>

<style scoped>
.el-button {
  margin-right: 10px;
}
</style>
