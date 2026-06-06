<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px" size="small">
<!--      <el-form-item label="基础日利率(如1%写0.010000)" prop="baseRatio">
        <el-input
          v-model="queryParams.baseRatio"
          placeholder="请输入基础日利率(如1%写0.010000)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="触发调节的涨跌幅阈值(百分比)" prop="triggerThreshold">
        <el-input
          v-model="queryParams.triggerThreshold"
          placeholder="请输入触发调节的涨跌幅阈值(百分比)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="每超出1%调整的日利率增量" prop="stepPerc">
        <el-input
          v-model="queryParams.stepPerc"
          placeholder="请输入每超出1%调整的日利率增量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="日利率下限" prop="minRatio">
        <el-input
          v-model="queryParams.minRatio"
          placeholder="请输入日利率下限"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="日利率上限" prop="maxRatio">
        <el-input
          v-model="queryParams.maxRatio"
          placeholder="请输入日利率上限"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否启用 1是0否" prop="enabled">
        <el-input
          v-model="queryParams.enabled"
          placeholder="请输入是否启用 1是0否"
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
      <el-col :span="1.5">
<!--        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['xms:dayRatioRule:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['xms:dayRatioRule:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['xms:dayRatioRule:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['xms:dayRatioRule:export']"
        >导出</el-button>-->
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dayRatioRuleList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column v-if="false" align="center" label="主键id" prop="id"/>
      <el-table-column align="center" label="币种类型" prop="coinType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_coin_type" :value="scope.row.coinType"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="基础日利率" prop="baseRatio" >
        <template slot-scope="scope">
          <span>{{ scope.row.baseRatio }} %</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="触发调节的涨跌幅阈值" prop="triggerThreshold" >
        <template slot-scope="scope">
          <span>{{ scope.row.triggerThreshold }} %</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="每超出1%调整的日利率增量" prop="stepPerc" >
        <template slot-scope="scope">
          <span>{{ scope.row.stepPerc }} %</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="日利率下限" prop="minRatio" >
        <template slot-scope="scope">
          <span>{{ scope.row.minRatio }} %</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="日利率上限" prop="maxRatio">
        <template slot-scope="scope">
          <span>{{ scope.row.maxRatio }} %</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="是否启用 1是0否" align="center" prop="enabled" />-->
<!--      <el-table-column label="备注" align="center" prop="remark" />-->
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
            v-hasPermi="['xms:dayRatioRule:edit']"
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
            v-hasPermi="['xms:dayRatioRule:remove']"
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

    <!-- 添加或修改BOOMAI日利率调节规则对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="基础日利率" prop="baseRatio">
          <el-input v-model="form.baseRatio"
                    oninput="value = value.replace(/^(\d+)(\.\d{0,2})?.*$/, '$1$2')"
                    placeholder="请输入基础日利率" />
          <div class="form-tip">以百分比为单位，例如：1 表示 1%</div>
        </el-form-item>
        <el-form-item label="触发调节的涨跌幅阈值" prop="triggerThreshold">
          <el-input v-model="form.triggerThreshold"
                    oninput="value = value.replace(/^(\d+)(\.\d{0,2})?.*$/, '$1$2')"
                    placeholder="请输入触发调节的涨跌幅阈值" />
          <div class="form-tip">以百分比为单位，例如：1 表示 1%</div>
        </el-form-item>
<!--        <el-form-item label="每超出1%调整的日利率增量" prop="stepPerc">
          <el-input v-model="form.stepPerc" placeholder="请输入每超出1%调整的日利率增量" />
        </el-form-item>-->
        <el-form-item label="日利率下限" prop="minRatio">
          <el-input v-model="form.minRatio"
                    oninput="value = value.replace(/^(\d+)(\.\d{0,2})?.*$/, '$1$2')"
                    placeholder="请输入日利率下限" />
          <div class="form-tip">以百分比为单位，例如：1 表示 1%</div>
        </el-form-item>
        <el-form-item label="日利率上限" prop="maxRatio">
          <el-input v-model="form.maxRatio"
                    oninput="value = value.replace(/^(\d+)(\.\d{0,2})?.*$/, '$1$2')"
                    placeholder="请输入日利率上限" />
          <div class="form-tip">以百分比为单位，例如：1 表示 1%</div>
        </el-form-item>
<!--        <el-form-item label="是否启用 1是0否" prop="enabled">
          <el-input v-model="form.enabled" placeholder="请输入是否启用 1是0否" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
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
import { listDayRatioRule, getDayRatioRule, delDayRatioRule, addDayRatioRule, updateDayRatioRule } from "@/api/xms/dayRatioRule";

export default {
  name: "DayRatioRule",
  dicts: ['t_coin_type'],
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
      // BOOMAI日利率调节规则表格数据
      dayRatioRuleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        coinType: null,
        baseRatio: null,
        triggerThreshold: null,
        stepPerc: null,
        minRatio: null,
        maxRatio: null,
        enabled: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        coinType: [
          { required: true, message: "币种类型，如 1=BOOMAI不能为空", trigger: "change" }
        ],
        baseRatio: [
          { required: true, message: "基础日利率不能为空", trigger: "blur" }
        ],
        triggerThreshold: [
          { required: true, message: "触发调节的涨跌幅阈值不能为空", trigger: "blur" }
        ],
        stepPerc: [
          { required: true, message: "每超出1%调整的日利率增量不能为空", trigger: "blur" }
        ],
        minRatio: [
          { required: true, message: "日利率下限不能为空", trigger: "blur" }
        ],
        maxRatio: [
          { required: true, message: "日利率上限不能为空", trigger: "blur" }
        ],
        enabled: [
          { required: true, message: "是否启用 1是0否不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "$comment不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询BOOMAI日利率调节规则列表 */
    getList() {
      this.loading = true;
      listDayRatioRule(this.queryParams).then(response => {
        this.dayRatioRuleList = response.rows;
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
        coinType: null,
        baseRatio: null,
        triggerThreshold: null,
        stepPerc: null,
        minRatio: null,
        maxRatio: null,
        enabled: null,
        remark: null,
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
      this.title = "添加BOOMAI日利率调节规则";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDayRatioRule(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改BOOMAI日利率规则";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDayRatioRule(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDayRatioRule(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除BOOMAI日利率调节规则编号为"' + ids + '"的数据项？').then(function() {
        return delDayRatioRule(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/dayRatioRule/export', {
        ...this.queryParams
      }, `dayRatioRule_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
