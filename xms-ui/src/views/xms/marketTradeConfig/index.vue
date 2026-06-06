<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px" size="small">
      <el-form-item label="市场" prop="name">
        <el-input
          v-model="queryParams.name"
          clearable
          placeholder="请输入市场"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品名称" prop="dataLabel">
        <el-input
          v-model="queryParams.dataLabel"
          clearable
          placeholder="请输入产品名称"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="是否订阅盘口" prop="dataPankou">
        <el-input
          v-model="queryParams.dataPankou"
          placeholder="请输入是否订阅盘口"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
<!--      <el-form-item label="交易对代码组合的" prop="dataCode">
        <el-input
          v-model="queryParams.dataCode"
          placeholder="请输入交易对代码组合的"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品品种类型 比如cl 原油 恒指 HL" prop="commodityNo">
        <el-input
          v-model="queryParams.commodityNo"
          placeholder="请输入产品品种类型 比如cl 原油 恒指 HL"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="合约代码" prop="contractNo">
        <el-input
          v-model="queryParams.contractNo"
          placeholder="请输入合约代码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="交易对名称" prop="dataLabel">
        <el-input
          v-model="queryParams.dataLabel"
          placeholder="请输入交易对名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="币单位/点，价格波动每点对应的价值" prop="price">
        <el-input
          v-model="queryParams.price"
          placeholder="请输入币单位/点，价格波动每点对应的价值"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="该产品币单位 根据xms_exchange_rate_config 获取" prop="priceUnit">
        <el-input
          v-model="queryParams.priceUnit"
          placeholder="请输入该产品币单位 根据xms_exchange_rate_config 获取"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="每天定时强平时间,24小时制" prop="closeTimeDay">
        <el-input
          v-model="queryParams.closeTimeDay"
          placeholder="请输入每天定时强平时间,24小时制"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="每月定时强平时间,24小时制" prop="closeTimeMonth">
        <el-input
          v-model="queryParams.closeTimeMonth"
          placeholder="请输入每月定时强平时间,24小时制"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="每手保证金" prop="securityDeposit">
        <el-input
          v-model="queryParams.securityDeposit"
          placeholder="请输入每手保证金"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
  -->
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
          v-hasPermi="['xms:marketTradeConfig:add']"
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
          v-hasPermi="['xms:marketTradeConfig:edit']"
        >修改</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:marketTradeConfig:export']"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="marketTradeConfigList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column v-if="false" align="center" label="id" prop="id"/>
      <el-table-column align="center" label="市场" prop="name" />
      <el-table-column align="center" label="市场代码" prop="type" />
      <el-table-column align="center" label="产品名称" prop="dataLabel" />
      <el-table-column align="center" label="是否订阅盘口" prop="dataPankou">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.dataPankou"
            :active-value="1"
            :inactive-value="0"
            active-color="#13ce66"
            inactive-color="#ff4949"
            @change="handleToggleDataPankou(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="产品名称繁体" prop="dataLabelHk" />
      <el-table-column align="center" label="产品名称英文" prop="dataLabelEn" />
      <el-table-column align="center" label="产品名称日文" prop="dataLabelJa" />
      <el-table-column align="center" label="产品名称韩文" prop="dataLabelKr" />
      <el-table-column align="center" label="创建时间" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="修改时间" prop="updateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>

<!--      <el-table-column label="是否订阅行情" align="center" prop="dataType"></el-table-column>
      <el-table-column label="交易对代码组合的" align="center" prop="dataCode" />
      <el-table-column label="产品品种类型 比如cl 原油 恒指 HL" align="center" prop="commodityNo" />


            <el-table-column label="备注" align="center" prop="remark" />-->
   <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['xms:marketTradeConfig:edit']"
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
            v-hasPermi="['xms:marketTradeConfig:remove']"
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

    <!-- 添加或修改交易产品行情数据管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="市场" prop="name">
          <el-input v-model="form.name" placeholder="请输入市场" />
        </el-form-item>

        <el-form-item label="市场代码" prop="type">
          <el-input v-model="form.type" placeholder="请输入市场代码" />
        </el-form-item>


        <el-form-item label="是否订阅盘口" prop="dataPankou">
          <el-select v-model="form.dataPankou" placeholder="请选择状态">
            <el-option
              v-for="dict in dict.type.t_user_info_is_valid"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>


        <el-form-item label="产品代码" prop="dataCode">
          <el-input v-model="form.dataCode" placeholder="请输入产品代码" />
        </el-form-item>

        <el-form-item label="产品名称名称" prop="dataLabel">
          <el-input v-model="form.dataLabel" placeholder="请输入交易对名称" />
        </el-form-item>


        <el-form-item label="交易对名称-繁体" prop="dataLabelHk">
          <el-input v-model="form.dataLabelHk" placeholder="请输入交易对名称" />
        </el-form-item>

        <el-form-item label="交易对名称-英文" prop="dataLabelEn">
          <el-input v-model="form.dataLabelEn" placeholder="请输入交易对名称" />
        </el-form-item>

        <el-form-item label="交易对名称-日文" prop="dataLabelJa">
          <el-input v-model="form.dataLabelJa" placeholder="请输入交易对名称" />
        </el-form-item>

        <el-form-item label="交易对名称-韩文" prop="dataLabelKr">
          <el-input v-model="form.dataLabelKr" placeholder="请输入交易对名称" />
        </el-form-item>

<!--

        <el-form-item label="币单位/点，价格波动每点对应的价值" prop="price">
          <el-input v-model="form.price" placeholder="请输入币单位/点，价格波动每点对应的价值" />
        </el-form-item>
        <el-form-item label="该产品币单位 根据xms_exchange_rate_config 获取" prop="priceUnit">
          <el-input v-model="form.priceUnit" placeholder="请输入该产品币单位 根据xms_exchange_rate_config 获取" />
        </el-form-item>
        <el-form-item label="每天定时强平时间,24小时制" prop="closeTimeDay">
          <el-input v-model="form.closeTimeDay" placeholder="请输入每天定时强平时间,24小时制" />
        </el-form-item>
        <el-form-item label="每月定时强平时间,24小时制" prop="closeTimeMonth">
          <el-input v-model="form.closeTimeMonth" placeholder="请输入每月定时强平时间,24小时制" />
        </el-form-item>
        <el-form-item label="每手保证金" prop="securityDeposit">
          <el-input v-model="form.securityDeposit" placeholder="请输入每手保证金" />
        </el-form-item>
-->

<!--        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>-->
<!--        <el-form-item label="是否删除 否 0  1 是" prop="deleted">
          <el-input v-model="form.deleted" placeholder="请输入是否删除 否 0  1 是" />
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
import { listMarketTradeConfig,
  handleDataPankou,
  getMarketTradeConfig, delMarketTradeConfig, addMarketTradeConfig, updateMarketTradeConfig } from "@/api/xms/marketTradeConfig";

export default {
  name: "MarketTradeConfig",
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
      // 交易产品行情数据管理表格数据
      marketTradeConfigList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        type: null,
        dataType: null,
        dataPankou: null,
        dataCode: null,
        commodityNo: null,
        contractNo: null,
        dataLabel: null

      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: " 市场 FX 外汇 Metal 贵金属  Futures  期货不能为空", trigger: "blur" }
        ],
        dataType: [
          { required: true, message: "是否订阅行情： 0 否 1 是不能为空", trigger: "change" }
        ],
        dataCode: [
          { required: true, message: "交易对代码组合的不能为空", trigger: "blur" }
        ],
        deleted: [
          { required: true, message: "是否删除 否 0  1 是不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询交易产品行情数据管理列表 */
    getList() {
      this.loading = true;
      listMarketTradeConfig(this.queryParams).then(response => {
        this.marketTradeConfigList = response.rows;
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
        name: null,
        type: null,
        dataType: null,
        dataPankou: null,
        dataCode: null,
        commodityNo: null,
        contractNo: null,
        dataLabel: null,
        dataLabelHk: null,
        dataLabelEn: null,
        dataLabelJa: null,
        dataLabelKr: null,



        remark: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        deleted: null
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
      this.title = "添加交易产品行情数据管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMarketTradeConfig(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改交易产品行情数据管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMarketTradeConfig(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMarketTradeConfig(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除交易产品行情数据管理编号为"' + ids + '"的数据项？').then(function() {
        return delMarketTradeConfig(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/marketTradeConfig/export', {
        ...this.queryParams
      }, `marketTradeConfig_${new Date().getTime()}.xlsx`)
    },
    /** 切换订阅盘口状态 */
    async handleToggleDataPankou(row) {
      try {
        await handleDataPankou(row.id, row.dataPankou);
        const message = row.dataPankou === 1 ? '订阅盘口成功' : '取消订阅盘口成功';
        this.$modal.msgSuccess(message);
      } catch (error) {
        console.error('切换订阅状态失败:', error);
        // 发生错误时恢复开关状态
        row.dataPankou = row.dataPankou === 1 ? 0 : 1;
        this.$modal.msgError('操作失败，请重试');
      }
    }
  }
};
</script>
