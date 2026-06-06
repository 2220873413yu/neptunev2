<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px" size="small">
<!--      <el-form-item label="加速后总释放天数" prop="targetDays">
        <el-input
          v-model="queryParams.targetDays"
          placeholder="请输入加速后总释放天数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="燃料币种" prop="fuelToken">
        <el-input
          v-model="queryParams.fuelToken"
          placeholder="请输入燃料币种"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所需燃料占本金比例，例如 10 = 10%" prop="fuelRatio">
        <el-input
          v-model="queryParams.fuelRatio"
          placeholder="请输入所需燃料占本金比例，例如 10 = 10%"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item>
        <el-button icon="el-icon-search" size="mini" type="primary" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['xms:accelerateReleaseConfig:add']"
        >新增</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:accelerateReleaseConfig:edit']"
          :disabled="single"
          icon="el-icon-edit"
          plain
          size="mini"
          type="success"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
<!--      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['xms:accelerateReleaseConfig:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['xms:accelerateReleaseConfig:export']"
        >导出</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="accelerateReleaseConfigList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column v-if="false" align="center" label="主键id" prop="id"/>
      <el-table-column align="center" label="加速后总释放天数" prop="targetDays" width="180"/>
      <el-table-column align="center" label="燃料币种" prop="fuelToken" />
      <el-table-column align="center" label="燃料占本金比例" prop="fuelRatio" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.fuelRatio }} %</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="是否启用" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_info_is_valid" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="备注" align="center" prop="remark" />-->
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['xms:accelerateReleaseConfig:edit']"
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
<!--          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:accelerateReleaseConfig:remove']"
          >删除</el-button>-->
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />

    <!-- 添加或修改收益加速释放配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="加速释放天数" prop="targetDays">
          <el-input v-model="form.targetDays"
                    oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
                    placeholder="请输入加速后总释放天数"/>
        </el-form-item>
        <el-form-item label="燃料币种" prop="fuelToken">
          <el-input v-model="form.fuelToken" :disabled="true" placeholder="请输入燃料币种"/>
        </el-form-item>

        <el-form-item label="是否启用" prop="status">
          <el-select v-model="form.status" placeholder="请选择是否启用">
            <el-option
              v-for="dict in dict.type.t_user_info_is_valid"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="所需燃料占本金比例" prop="fuelRatio">
          <el-input v-model="form.fuelRatio"
                    oninput="value = value.replace(/^(\d+)(\.\d{0,2})?.*$/, '$1$2')"
                    placeholder="请输入例如 10 = 10%" />
          <div class="form-tip">以百分比为单位，例如：1 表示 1%</div>
        </el-form-item>
<!--        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="删除标记，0-未删除，1-已删除" prop="deleted">
          <el-input v-model="form.deleted" placeholder="请输入删除标记，0-未删除，1-已删除" />
        </el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAccelerateReleaseConfig, getAccelerateReleaseConfig, delAccelerateReleaseConfig, addAccelerateReleaseConfig, updateAccelerateReleaseConfig } from "@/api/xms/accelerateReleaseConfig";

export default {
  name: "AccelerateReleaseConfig",
  dicts: ['t_user_info_is_valid'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 收益加速释放配置表格数据
      accelerateReleaseConfigList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        targetDays: null,
        fuelToken: null,
        fuelRatio: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        targetDays: [
          { required: true, message: "加速后总释放天数不能为空", trigger: "blur" }
        ],
        fuelToken: [
          { required: true, message: "燃料币种不能为空", trigger: "blur" }
        ],
        fuelRatio: [
          { required: true, message: "所需燃料占本金比例，例如 10 = 10%不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询收益加速释放配置列表 */
    getList() {
      this.loading = true;
      listAccelerateReleaseConfig(this.queryParams).then(response => {
        this.accelerateReleaseConfigList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        targetDays: null,
        fuelToken: null,
        fuelRatio: null,
        status: null,
        createTime: null,
        updateTime: null,
        remark: null,
        deleted: null,
        createBy: null,
        updateBy: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加收益加速释放配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAccelerateReleaseConfig(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改收益加速释放配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAccelerateReleaseConfig(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAccelerateReleaseConfig(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除收益加速释放配置编号为"' + ids + '"的数据项？').then(function() {
        return delAccelerateReleaseConfig(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/accelerateReleaseConfig/export', {
        ...this.queryParams
      }, `accelerateReleaseConfig_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
