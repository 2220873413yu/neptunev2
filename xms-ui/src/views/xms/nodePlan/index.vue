<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px" size="small">
      <el-form-item label="节点等级" prop="nodeLevel">
        <el-select v-model="queryParams.nodeLevel" clearable placeholder="请选择节点等级">
          <el-option
            v-for="dict in dict.type.t_node_plan_node_level"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

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
          v-hasPermi="['xms:nodePlan:add']"
        >新增</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:nodePlan:edit']"
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
          v-hasPermi="['xms:nodePlan:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['xms:nodePlan:export']"
        >导出</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="nodePlanList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column v-if="false" align="center" label="主键id" prop="id"/>
      <el-table-column align="center" label="节点等级" prop="nodeLevel">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_node_plan_node_level" :value="scope.row.nodeLevel"/>
        </template>
      </el-table-column>


<!--      <el-table-column align="center" label="节点名称_简体中文" prop="nodeNameHk" />-->

<!--      <el-table-column label="节点名称_英文" align="center" prop="nodeNameEn" />
      <el-table-column label="节点名称_日本語" align="center" prop="nodeNameJa" />
      <el-table-column label="节点名称_韩文" align="center" prop="nodeNameKr" />
      <el-table-column label="节点名称_泰文" align="center" prop="nodeNameTh" />
      <el-table-column label="节点名称_越南" align="center" prop="nodeNameVi" />-->
      <el-table-column align="center" label="认购金额" prop="purchaseAmount" />
<!--      <el-table-column align="center" label="销量" prop="soldQuota" />-->
      <el-table-column align="center" label="年化收益率" prop="annualRate" >
        <template slot-scope="scope">
          <span>{{ scope.row.annualRate}} %</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="工作室补贴" prop="studioSubsidyRatio" >
        <template slot-scope="scope">
          <span>{{ scope.row.studioSubsidyRatio}} %</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="权重系数" prop="weightCoefficient" />
      <el-table-column align="center" label="是否上架" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_info_is_valid" :value="scope.row.status"/>
        </template>
      </el-table-column>
<!--      <el-table-column align="center" label="排序值" prop="sortOrder" />-->
      <el-table-column align="center" label="创建时间" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="更新时间" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['xms:nodePlan:edit']"
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
            v-hasPermi="['xms:nodePlan:remove']"
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

    <!-- 添加或修改认购节点配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="660px">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-divider content-position="left">基础设置</el-divider>
        <el-row :gutter="16" class="section-row">
          <el-col :span="12">
            <el-form-item label="节点等级" prop="nodeLevel">
              <el-select v-model="form.nodeLevel" placeholder="请选择节点等级">
                <el-option
                  v-for="dict in dict.type.t_node_plan_node_level"
                  :key="dict.value"
                  :disabled="true"
                  :label="dict.label"
                  :value="parseInt(dict.value)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="认购金额" prop="purchaseAmount">
              <el-input v-model="form.purchaseAmount"
                        :disabled="true"
                        oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
                        placeholder="请输入认购金额" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="16" class="section-row">
<!--          <el-col :span="12">
            <el-form-item label="总库存" prop="totalQuota">
              <el-input v-model="form.totalQuota"
                        oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
                        placeholder="请输入总库存" />
            </el-form-item>
          </el-col>-->
          <el-col :span="12">
            <el-form-item label="工作室补贴" prop="studioSubsidyRatio">
              <el-input v-model="form.studioSubsidyRatio"
                        oninput="value = value.replace(/^(\d+)(\.\d{0,2})?.*$/, '$1$2')"
                        placeholder="请输入补贴比例" />

              <div class="form-tip">以百分比为单位，例如：1 表示 1%</div>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="权重系数" prop="weightCoefficient">
              <el-input v-model="form.weightCoefficient"
                        oninput="value = value.replace(/^(\d+)(\.\d{0,2})?.*$/, '$1$2')"
                        placeholder="请输入权重系数" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="年化收益率" prop="annualRate">
              <el-input v-model="form.annualRate"
                        oninput="value = value.replace(/^(\d+)(\.\d{0,2})?.*$/, '$1$2')"
                        placeholder="年化收益率" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="16" class="section-row">

<!--          <el-col :span="12">
            <el-form-item label="排序值" prop="sortOrder">
              <el-input v-model="form.sortOrder"
                        oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
                        placeholder="请输入排序值" />
            </el-form-item>
          </el-col>-->

<!--          <el-col :span="12">
            <el-form-item label="确认成功的销量" prop="soldQuota">
              <el-input v-model="form.soldQuota"
                        :disabled="true"
                        placeholder="请输入确认成功的销量" />
            </el-form-item>
          </el-col>-->
        </el-row>

<!--        <el-divider content-position="left">多语言名称</el-divider>
        <el-row :gutter="16" class="section-row">
          <el-col :span="12">
            <el-form-item label="节点名称_英文" prop="nodeNameEn">
              <el-input v-model="form.nodeNameEn"
                        maxlength="30" placeholder="请输入节点名称_英文"
                        show-word-limit />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="节点名称_简体中文" prop="nodeNameHk">
              <el-input v-model="form.nodeNameHk"
                        maxlength="30" placeholder="请输入节点名称_简体中文"
                        show-word-limit />
            </el-form-item>
          </el-col>
        </el-row>-->



      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listNodePlan, getNodePlan, delNodePlan, addNodePlan, updateNodePlan } from "@/api/xms/nodePlan";

export default {
  name: "NodePlan",
  dicts: ['t_node_plan_node_level','t_user_info_is_valid'],
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
      // 认购节点配置表格数据
      nodePlanList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        nodeLevel: null,
        nodeNameEn: null,
        nodeNameHk: null,
        nodeNameJa: null,
        nodeNameKr: null,
        nodeNameTh: null,
        nodeNameVi: null,
        purchaseAmount: null,
        totalQuota: null,
        availableQuota: null,
        lockedQuota: null,
        soldQuota: null,
        studioSubsidyRatio: null,
        weightCoefficient: null,
        status: null,
        sortOrder: null,
        createTime: null,
        updateTime: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        nodeNameEn: [
          { required: true, message: "节点名称不能为空", trigger: "blur" }
        ],
        nodeNameHk: [
          { required: true, message: "节点名称不能为空", trigger: "blur" }
        ],
        annualRate: [
          { required: true, message: "年化收益率不能为空", trigger: "blur" }
        ],
        nodeNameJa: [
          { required: true, message: "节点名称不能为空", trigger: "blur" }
        ],
        nodeNameKr: [
          { required: true, message: "节点名称不能为空", trigger: "blur" }
        ],
        nodeNameTh: [
          { required: true, message: "节点名称不能为空", trigger: "blur" }
        ],
        nodeNameVi: [
          { required: true, message: "节点名称不能为空", trigger: "blur" }
        ],
        purchaseAmount: [
          { required: true, message: "认购金额不能为空", trigger: "blur" }
        ],
        totalQuota: [
          { required: true, message: "总库存不能为空", trigger: "blur" }
        ],
        availableQuota: [
          { required: true, message: "可售库存不能为空", trigger: "blur" }
        ],
        lockedQuota: [
          { required: true, message: "锁定库存不能为空", trigger: "blur" }
        ],
        soldQuota: [
          { required: true, message: "确认成功的销量不能为空", trigger: "blur" }
        ],
        studioSubsidyRatio: [
          { required: true, message: "工作室补贴比例不能为空", trigger: "blur" }
        ],
        weightCoefficient: [
          { required: true, message: "权重系数不能为空", trigger: "blur" }
        ],
        sortOrder: [
          { required: true, message: "排序不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询认购节点配置列表 */
    getList() {
      this.loading = true;
      listNodePlan(this.queryParams).then(response => {
        this.nodePlanList = response.rows;
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
        nodeLevel: null,
        nodeNameEn: null,
        nodeNameHk: null,
        nodeNameJa: null,
        nodeNameKr: null,
        nodeNameTh: null,
        nodeNameVi: null,
        purchaseAmount: null,
        totalQuota: null,
        availableQuota: null,
        lockedQuota: null,
        annualRate: null,
        soldQuota: null,
        studioSubsidyRatio: null,
        weightCoefficient: null,
        status: null,
        sortOrder: null,
        createTime: null,
        updateTime: null
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
      this.title = "添加认购节点配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getNodePlan(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改认购节点配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateNodePlan(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addNodePlan(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除认购节点配置编号为"' + ids + '"的数据项？').then(function() {
        return delNodePlan(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/nodePlan/export', {
        ...this.queryParams
      }, `nodePlan_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
