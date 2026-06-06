<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px" size="small">
      <el-form-item label="轮次编号" prop="roundNo">
        <el-input
          v-model="queryParams.roundNo"
          clearable
          placeholder="请输入轮次编号"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" clearable placeholder="请选择">
          <el-option
            v-for="dict in dict.type.t_airdrop_round_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="本轮总可领取次数" prop="totalQuota">
        <el-input
          v-model="queryParams.totalQuota"
          placeholder="请输入本轮总可领取次数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="已成功领取次数" prop="claimedQuota">
        <el-input
          v-model="queryParams.claimedQuota"
          placeholder="请输入已成功领取次数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="已锁定未完成次数" prop="lockedQuota">
        <el-input
          v-model="queryParams.lockedQuota"
          placeholder="请输入已锁定未完成次数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="每次领取代币数量" prop="tokenPerClaim">
        <el-input
          v-model="queryParams.tokenPerClaim"
          placeholder="请输入每次领取代币数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="本轮发完是否自动开下一轮 0否 1是" prop="autoOpenNext">
        <el-input
          v-model="queryParams.autoOpenNext"
          placeholder="请输入本轮发完是否自动开下一轮 0否 1是"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="预设下一轮编号，可为空" prop="nextRoundNo">
        <el-input
          v-model="queryParams.nextRoundNo"
          placeholder="请输入预设下一轮编号，可为空"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input
          v-model="queryParams.sort"
          placeholder="请输入排序"
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
        <el-button
          v-hasPermi="['xms:airdropRound:add']"
          icon="el-icon-plus"
          plain
          size="mini"
          type="primary"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:airdropRound:edit']"
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
          v-hasPermi="['xms:airdropRound:remove']"
        >删除</el-button>
      </el-col>-->
<!--      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['xms:airdropRound:export']"
        >导出</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="airdropRoundList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column v-if="false" align="center" label="主键" prop="id"/>
      <el-table-column align="center" label="轮次编号" prop="roundNo" />
      <el-table-column align="center" label="状态" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_airdrop_round_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="本轮总可领取次数" prop="totalQuota" />
      <el-table-column align="center" label="已成功领取次数" prop="claimedQuota" />
      <el-table-column align="center" label="已锁定未完成次数" prop="lockedQuota" />
      <el-table-column align="center" label="每次领取代币数量" prop="tokenPerClaim" />
      <el-table-column align="center" label="领取需支付价值多少u的OKB数量" prop="okbPayAmount" />
      <el-table-column align="center" label="是否自动开下一轮" prop="autoOpenNext" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_info_is_valid" :value="scope.row.autoOpenNext"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="预设下一轮编号，可为空" prop="nextRoundNo" />
      <el-table-column align="center" label="创建时间" prop="createTime" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="修改时间" prop="updateTime" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="备注" align="center" prop="remark" />-->
<!--      <el-table-column label="排序" align="center" prop="sort" />-->
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status!=2"
            v-hasPermi="['xms:airdropRound:edit']"
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
            v-hasPermi="['xms:airdropRound:remove']"
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

    <!-- 添加或修改空投轮次配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="600px">
      <el-form ref="form" :model="form" :rules="rules" label-position="top">
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="轮次编号" prop="roundNo">
              <el-input v-model="form.roundNo" :disabled="true" placeholder="轮次编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态">
                <el-option
                  v-for="dict in dict.type.t_airdrop_round_status"
                  :key="dict.value"
                  :disabled="parseInt(dict.value) === 2"
                  :label="dict.label"
                  :value="parseInt(dict.value)"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="本轮总可领取次数" prop="totalQuota">
              <el-input-number v-model="form.totalQuota" :min="0" :step="1" controls-position="right" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="已成功领取次数" prop="claimedQuota">
              <el-input-number v-model="form.claimedQuota" :disabled="true" :min="0" :step="1" controls-position="right" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="已锁定未完成次数" prop="lockedQuota">
              <el-input-number v-model="form.lockedQuota" :disabled="true" :min="0" :step="1" controls-position="right" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="每次领取代币数量" prop="tokenPerClaim">
              <el-input-number v-model="form.tokenPerClaim" :min="0" :precision="4" :step="1" controls-position="right" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="领取需支付价值多少u的OKB数量" prop="okbPayAmount">
              <el-input-number v-model="form.okbPayAmount" :min="0" :precision="4"

                               :step="1" controls-position="right" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="本轮发完是否自动开下一轮" prop="autoOpenNext">
              <el-select v-model="form.autoOpenNext" placeholder="请选择">
                <el-option
                  v-for="dict in dict.type.t_user_info_is_valid"
                  :key="dict.value"
                  :label="dict.label"
                  :value="parseInt(dict.value)"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col v-if="form.autoOpenNext == 1" :span="12">
            <el-form-item label="下一轮编号" prop="nextRoundNo">
              <el-input v-model="form.nextRoundNo" placeholder="请输入下一轮编号" />
            </el-form-item>
          </el-col>
        </el-row>

<!--        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" :step="1" controls-position="right" placeholder="请输入排序" />
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
import { listAirdropRound, getAirdropRound, delAirdropRound, addAirdropRound, updateAirdropRound } from "@/api/xms/airdropRound";

export default {
  name: "AirdropRound",
  dicts: ['t_airdrop_round_status','t_user_info_is_valid'],
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
      // 空投轮次配置表格数据
      airdropRoundList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        roundNo: null,
        status: null,
        totalQuota: null,
        claimedQuota: null,
        lockedQuota: null,
        tokenPerClaim: null,
        autoOpenNext: null,
        nextRoundNo: null,
        sort: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        status: [
          { required: true, message: "0待启用 1启用 2关闭不能为空", trigger: "change" }
        ],
        totalQuota: [
          { required: true, message: "本轮总可领取次数不能为空", trigger: "blur" }
        ],
        claimedQuota: [
          { required: true, message: "已成功领取次数不能为空", trigger: "blur" }
        ],
        lockedQuota: [
          { required: true, message: "已锁定未完成次数不能为空", trigger: "blur" }
        ],
        tokenPerClaim: [
          { required: true, message: "每次领取代币数量不能为空", trigger: "blur" }
        ],
        okbPayAmount: [
          { required: true, message: "领取需支付价值多少u的OKB数量不能为空", trigger: "blur" }
        ],
        autoOpenNext: [
          { required: true, message: "本轮发完是否自动开下一轮 0否 1是不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询空投轮次配置列表 */
    getList() {
      this.loading = true;
      listAirdropRound(this.queryParams).then(response => {
        this.airdropRoundList = response.rows;
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
        roundNo: null,
        status: null,
        totalQuota: null,
        claimedQuota: null,
        lockedQuota: null,
        tokenPerClaim: null,
        okbPayAmount: null,
        autoOpenNext: null,
        nextRoundNo: null,
        remark: null,
        createTime: null,
        updateTime: null,
        sort: null
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
      this.title = "添加空投轮次配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAirdropRound(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改空投轮次配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAirdropRound(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAirdropRound(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除空投轮次配置编号为"' + ids + '"的数据项？').then(function() {
        return delAirdropRound(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/airdropRound/export', {
        ...this.queryParams
      }, `airdropRound_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
