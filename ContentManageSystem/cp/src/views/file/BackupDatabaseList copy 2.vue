<script lang="ts">
export default { name: 'BackupDatabaseList' };
</script>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { Plus, Delete, ArrowDown } from '@element-plus/icons-vue';
import { useI18n } from 'vue-i18n';
import { useRoute } from 'vue-router';
import { perm } from '@/stores/useCurrentUser';
import axios from 'axios';
import dayjs from 'dayjs';


const { t } = useI18n();
const params = ref<any>({});
const sort = ref<any>();
const currentPage = ref<number>(1);
const pageSize = ref<number>(10);
const total = ref<number>(0);
const table = ref<any>();
const data = ref<any[]>([]);
const selection = ref<any[]>([]);
const loading = ref<boolean>(false);
const formVisible = ref<boolean>(false);
const beanId = ref<number>();
const beanIds = computed(() => data.value.map((row) => row.id));


const backups = ref([]);


const handleSort = ({ column, prop, order }: { column: any; prop: string; order: string }) => {
  if (prop && order) {
    sort.value = (column.sortBy ?? prop) + (order === 'descending' ? '_desc' : '');
  } else {
    sort.value = undefined;
  }
  fetchData();
};
const handleSearch = () => fetchData();
const handleReset = () => {
  table.value.clearSort();
  resetParams(params.value);
  sort.value = undefined;
  fetchData();
};

const handleAdd = () => {
  beanId.value = undefined;
  formVisible.value = true;
};
const handleEdit = (id: number) => {
  beanId.value = id;
  formVisible.value = true;
};



const fetchBackups = async () => {
  try {
    // 使用静态数据替代API调用
    backups.value = [
      { id: 1, name: '备份一', date: '2022-01-01 12:00' },
      { id: 2, name: '备份二', date: '2022-02-01 13:00' },
      { id: 3, name: '备份三', date: '2022-03-01 14:00' }
    ];
    ElMessage.success('备份列表加载成功');
  } catch (error) {
    ElMessage.error('获取备份列表失败: ' + error.message);
  }
};

// const fetchBackups = async () => {
//   try {
//     const response = await axios.get('/backend/ext/backup-database');
//     backups.value = response.data;
//   } catch (error) {
//     ElMessage.error('获取备份列表失败: ' + error.message);
//   }
// };

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

const handleDelete = async (ids: number[]) => {
  // await deleteMessageBoard(ids);
  // fetchData();
  // ElMessage.success(t('success'));
};

onMounted(fetchBackups);
</script>


<template>
  <div>
    <div class="mb-3">
      <div class="space-x-2">
        <el-popconfirm :title="$t('confirmBackup')" :width="180" @confirm="() => handleAdd()">{{ $t('backup') }}>
          <template #reference>
            <el-button type="primary" :icon="Plus">{{ $t('backup') }}</el-button>
          </template>
        </el-popconfirm>
        <el-popconfirm :title="$t('confirmDelete')" @confirm="() => handleDelete(selection.map((row) => row.id))">
          <template #reference>
            <el-button :disabled="selection.length <= 0" :icon="Delete">{{ $t('delete') }}</el-button>
          </template>
        </el-popconfirm>

      </div>
    </div>


    <div class="app-block">
      <el-table ref="table" v-loading="loading" :data="backups" @selection-change="(rows) => (selection = rows)"
        @sort-change="handleSort">

        <el-table-column type="selection" width="38"></el-table-column>
        <el-table-column prop="id" label="备份ID" width="180" sortable="custom" ></el-table-column>
        <el-table-column prop="name" label="备份名称" width="280" sortable="custom" ></el-table-column>
        <el-table-column prop="date" label="备份时间" width="180" sortable="custom" ></el-table-column>
        <el-table-column label="操作">
          <template #default="{ row }">
            <el-button size="mini" @click="restoreBackup(row.name)">恢复</el-button>
            <el-button size="mini" type="danger" @click="deleteBackup(row)">删除</el-button>
          </template>
        </el-table-column>

      </el-table>
    </div>

  </div>
</template>
