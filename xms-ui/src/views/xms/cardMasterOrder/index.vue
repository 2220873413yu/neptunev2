<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px" size="small">
      <el-form-item label="主订单号" prop="masterOrderNo">
        <el-input
          v-model="queryParams.masterOrderNo"
          clearable
          placeholder="请输入主订单号"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户ID" label-width="120px" prop="userId">
        <el-input
          v-model="queryParams.userId"
          clearable
          placeholder="请输入用户ID"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="本次实际支付金额" prop="payAmount">
        <el-input
          v-model="queryParams.payAmount"
          placeholder="请输入本次实际支付金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="本次订单奖励算力" prop="computingPower">
        <el-input
          v-model="queryParams.computingPower"
          placeholder="请输入本次订单奖励算力"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="卡片类型" label-width="120px" prop="cardType">
        <el-select v-model="queryParams.cardType" clearable placeholder="请选择卡片类型">
          <el-option
            v-for="dict in dict.type.card_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="来源类型" label-width="120px" prop="sourceType">
        <el-select v-model="queryParams.sourceType" clearable placeholder="请选择来源类型">
          <el-option
            v-for="dict in dict.type.card_order_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="购买数量" prop="buyNum">
        <el-input
          v-model="queryParams.buyNum"
          placeholder="请输入购买数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="创建时间" label-width="120px">
        <el-date-picker
          v-model="daterangeCreateTime"
          end-placeholder="结束日期"
          range-separator="-"
          start-placeholder="开始日期"
          style="width: 240px"
          type="datetimerange"
          value-format="yyyy-MM-dd HH:mm:ss"
        ></el-date-picker>
      </el-form-item>
<!--      <el-form-item label="购买的时候快照 json" prop="snapshotJson">
        <el-input
          v-model="queryParams.snapshotJson"
          placeholder="请输入购买的时候快照 json"
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
          v-hasPermi="['xms:cardMasterOrder:add']"
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
          v-hasPermi="['xms:cardMasterOrder:edit']"
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
          v-hasPermi="['xms:cardMasterOrder:remove']"
        >删除</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:cardMasterOrder:export']"
          icon="el-icon-download"
          plain
          size="mini"
          type="warning"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="cardMasterOrderList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
<!--      <el-table-column label="主键ID" align="center" prop="id" />-->
      <el-table-column align="center" label="主订单号" prop="masterOrderNo" />
      <el-table-column align="center" label="用户ID" prop="userId" />
      <el-table-column align="center" label="本次支付金额" prop="payAmount" />
      <el-table-column align="center" label="本次订单算力" prop="computingPower" />
      <el-table-column align="center" label="补贴算力" prop="extraComputingPower" />
      <el-table-column align="center" label="卡片类型" prop="cardType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.card_type" :value="scope.row.cardType"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="来源类型" prop="sourceType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.card_order_type" :value="scope.row.sourceType"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="购买数量" prop="buyNum" />
      <el-table-column align="center" label="创建时间" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="购买的时候快照 json" align="center" prop="snapshotJson" />-->
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['xms:cardMasterOrder:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:cardMasterOrder:remove']"
          >删除</el-button>
        </template>
      </el-table-column>-->
    </el-table>

    <pagination
      v-show="total>0"
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />

    <!-- 添加或修改购买记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="主订单号" prop="masterOrderNo">
          <el-input v-model="form.masterOrderNo" placeholder="请输入主订单号" />
        </el-form-item>
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="本次实际支付金额" prop="payAmount">
          <el-input v-model="form.payAmount" placeholder="请输入本次实际支付金额" />
        </el-form-item>
        <el-form-item label="本次订单奖励算力" prop="computingPower">
          <el-input v-model="form.computingPower" placeholder="请输入本次订单奖励算力" />
        </el-form-item>
        <el-form-item label="卡片类型 1:普通卡,2:白银卡,3白金卡,4:黑金卡" prop="cardType">
          <el-select v-model="form.cardType" placeholder="请选择卡片类型 1:普通卡,2:白银卡,3白金卡,4:黑金卡">
            <el-option
              v-for="dict in dict.type.card_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="购买数量" prop="buyNum">
          <el-input v-model="form.buyNum" placeholder="请输入购买数量" />
        </el-form-item>
        <el-form-item label="购买的时候快照 json" prop="snapshotJson">
          <el-input v-model="form.snapshotJson" placeholder="请输入购买的时候快照 json" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCardMasterOrder, getCardMasterOrder, delCardMasterOrder, addCardMasterOrder, updateCardMasterOrder } from "@/api/xms/cardMasterOrder";

export default {
  name: "CardMasterOrder",
  dicts: ['card_type','card_order_type'],
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
      // 购买记录表格数据
      cardMasterOrderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 购买的时候快照 json时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        masterOrderNo: null,
        userId: null,
        payAmount: null,
        computingPower: null,
        cardType: null,
        buyNum: null,
        createTime: null,
        snapshotJson: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        payAmount: [
          { required: true, message: "本次实际支付金额不能为空", trigger: "blur" }
        ],
        cardType: [
          { required: true, message: "卡片类型 1:普通卡,2:白银卡,3白金卡,4:黑金卡不能为空", trigger: "change" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询购买记录列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listCardMasterOrder(this.queryParams).then(response => {
        this.cardMasterOrderList = response.rows;
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
        masterOrderNo: null,
        userId: null,
        payAmount: null,
        computingPower: null,
        cardType: null,
        buyNum: null,
        createTime: null,
        updateTime: null,
        snapshotJson: null
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
      this.daterangeCreateTime = [];
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
      this.title = "添加购买记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCardMasterOrder(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改购买记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCardMasterOrder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCardMasterOrder(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除购买记录编号为"' + ids + '"的数据项？').then(function() {
        return delCardMasterOrder(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/cardMasterOrder/export', {
        ...this.queryParams
      }, `cardMasterOrder_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
