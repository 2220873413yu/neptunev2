<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px" size="small">
<!--      <el-form-item label="基金名称_cn" prop="nameCn">
        <el-input
          v-model="queryParams.nameCn"
          placeholder="请输入基金名称_cn"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="基金类型 0:活期,1:定期" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择基金类型 0:活期,1:定期" clearable>
          <el-option
            v-for="dict in dict.type.t_w3_mining_package_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="基金有效期天数" prop="day">
        <el-input
          v-model="queryParams.day"
          placeholder="请输入基金有效期天数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="日利率" prop="dayRatio">
        <el-input
          v-model="queryParams.dayRatio"
          placeholder="请输入日利率"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="最少购买金额限制" prop="minBuyPrice">
        <el-input
          v-model="queryParams.minBuyPrice"
          placeholder="请输入最少购买金额限制"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="每日违约金递减率(如0.5%)" prop="dailyPenaltyReduction">
        <el-input
          v-model="queryParams.dailyPenaltyReduction"
          placeholder="请输入每日违约金递减率(如0.5%)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="违约金比例(如20%)" prop="penaltyRate">
        <el-input
          v-model="queryParams.penaltyRate"
          placeholder="请输入违约金比例(如20%)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="基金名称_hk 繁体" prop="nameHk">
        <el-input
          v-model="queryParams.nameHk"
          placeholder="请输入基金名称_hk 繁体"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="基金名称_en 英文" prop="nameEn">
        <el-input
          v-model="queryParams.nameEn"
          placeholder="请输入基金名称_en 英文"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="基金名称_ja 日文" prop="nameJa">
        <el-input
          v-model="queryParams.nameJa"
          placeholder="请输入基金名称_ja 日文"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="基金名称_kr 韩文" prop="nameKr">
        <el-input
          v-model="queryParams.nameKr"
          placeholder="请输入基金名称_kr 韩文"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="是否上架" prop="status">
        <el-select v-model="queryParams.status" clearable placeholder="请选择是否上架">
          <el-option
            v-for="dict in dict.type.t_user_info_is_valid"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="排序" prop="sort">
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
<!--      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['xms:miningPackage:add']"
        >新增</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['xms:miningPackage:edit']"
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
          v-hasPermi="['xms:miningPackage:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['xms:miningPackage:export']"
        >导出</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="miningPackageList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column v-if="false" align="center" label="主键id" prop="id"/>
      <el-table-column align="center" label="基金名称" prop="nameCn" />
      <el-table-column align="center" label="日利率" prop="dayRatio" >
        <template slot-scope="scope">
        {{scope.row.dayRatio}}%
        </template>
      </el-table-column>
      <el-table-column align="center" label="基金类型" prop="type">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_w3_mining_package_type" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="天数" prop="day" />
<!--      <el-table-column label="销量" align="center" prop="buyNum" />-->
      <el-table-column align="center" label="最少购买金额限制" prop="minBuyPrice" />
      <el-table-column align="center" label="每日违约金递减率" prop="dailyPenaltyReduction" >
        <template slot-scope="scope">
          {{scope.row.dailyPenaltyReduction}}%
        </template>
      </el-table-column>
      <el-table-column align="center" label="违约金比例" prop="penaltyRate">
      <template slot-scope="scope">
        {{scope.row.penaltyRate}}%
      </template>
      </el-table-column>
      <el-table-column align="center" label="最低违约金比例" prop="minPenaltyRate" >
      <template slot-scope="scope">
        {{scope.row.minPenaltyRate}}%
      </template>
      </el-table-column>
<!--      <el-table-column label="基金名称_hk 繁体" align="center" prop="nameHk" />
      <el-table-column label="基金名称_en 英文" align="center" prop="nameEn" />
      <el-table-column label="基金名称_ja 日文" align="center" prop="nameJa" />
      <el-table-column label="基金名称_kr 韩文" align="center" prop="nameKr" />-->
      <el-table-column align="center" label="是否上架" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_info_is_valid" :value="scope.row.status"/>
        </template>
      </el-table-column>
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
<!--      <el-table-column label="排序" align="center" prop="sort" />
      <el-table-column label="描述_中文" align="center" prop="descriptionCn" />
      <el-table-column label="描述_hk 繁体" align="center" prop="descriptionHk" />
      <el-table-column label="描述_en 英文" align="center" prop="descriptionEn" />
      <el-table-column label="描述_ja 日文" align="center" prop="descriptionJa" />
      <el-table-column label="描述_kr 韩文" align="center" prop="descriptionKr" />-->
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            v-hasPermi="['xms:miningPackage:edit']"
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
            v-hasPermi="['xms:miningPackage:remove']"
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

    <!-- 添加或修改基金套餐对话框 -->
    <el-dialog :title="title" :visible.sync="open" append-to-body width="900px">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <!-- 基础信息 -->
        <div class="form-section">
          <h3 class="section-title">基础信息</h3>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="基金名称" prop="nameCn">
                <el-input v-model="form.nameCn"
                          maxlength="100" placeholder="请输入基金名称"
                          show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="基金类型" prop="type">
                <el-select v-model="form.type" placeholder="请选择基金类型" style="width: 100%" >
                  <el-option
                    v-for="dict in dict.type.t_w3_mining_package_type"
                    :key="dict.value"
                    :disabled="true"
                    :label="dict.label"
                    :value="parseInt(dict.value)"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col v-if="form.type == 1 || form.type == 2" :span="12">
              <el-form-item label="有效期天数" prop="day">
                <el-input v-model="form.day"
                          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
                          placeholder="请输入基金有效期天数" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="日利率" prop="dayRatio">
                <el-input v-model="form.dayRatio"
                          oninput="if(!/^\d*\.?\d*$/.test(value)) { value = value.replace(/[^\d.]/g, ''); }"
                          placeholder="请输入日利率" />
                <div class="form-tip">以百分比为单位，例如：1 表示 1%</div>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="最少购买金额" prop="minBuyPrice">
                <el-input v-model="form.minBuyPrice"
                          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
                          placeholder="请输入最少购买金额" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="最大购买金额" prop="maxBuyPrice">
                <el-input v-model="form.maxBuyPrice"
                          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
                          placeholder="请输入最大购买金额" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="是否上架" prop="status">
                <el-select v-model="form.status" placeholder="请选择是否上架" style="width: 100%">
                  <el-option
                    v-for="dict in dict.type.t_user_info_is_valid"
                    :key="dict.value"
                    :label="dict.label"
                    :value="parseInt(dict.value)"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

<!--          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="排序" prop="sort">
                <el-input v-model="form.sort"
                          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
                          placeholder="请输入排序" />
              </el-form-item>
            </el-col>
          </el-row>-->
        </div>

        <!-- 违约金设置 (仅定期基金显示) -->
        <div v-if="form.type == 1" class="form-section">
          <h3 class="section-title">违约金设置</h3>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="违约金比例" prop="penaltyRate">
                <el-input v-model="form.penaltyRate"
                          oninput="if(!/^\d*\.?\d*$/.test(value)) { value = value.replace(/[^\d.]/g, ''); }"
                          placeholder="如：20 (表示20%)" />
                <div class="form-tip">以百分比为单位，例如：1 表示 1%</div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="每日递减率" prop="dailyPenaltyReduction">
                <el-input v-model="form.dailyPenaltyReduction"
                          oninput="if(!/^\d*\.?\d*$/.test(value)) { value = value.replace(/[^\d.]/g, ''); }"
                          placeholder="如：0.5 (表示0.5%)" />
                <div class="form-tip">以百分比为单位，例如：1 表示 1%</div>
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="最低违约金比例" prop="minPenaltyRate">
                <el-input v-model="form.minPenaltyRate"
                          oninput="if(!/^\d*\.?\d*$/.test(value)) { value = value.replace(/[^\d.]/g, ''); }"
                          placeholder="如：30 (表示30%)" />
                <div class="form-tip">以百分比为单位，例如：1 表示 1%</div>
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 多语言名称 -->
        <div class="form-section">
          <h3 class="section-title">多语言名称</h3>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="繁体中文" prop="nameHk">
                <el-input v-model="form.nameHk"
                          maxlength="100" placeholder="请输入繁体中文名称"
                          show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="英文" prop="nameEn">
                <el-input v-model="form.nameEn"
                          maxlength="100" placeholder="请输入英文名称"
                          show-word-limit />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="日文" prop="nameJa">
                <el-input v-model="form.nameJa"
                          maxlength="100" placeholder="请输入日文名称"
                          show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="韩文" prop="nameKr">
                <el-input v-model="form.nameKr"
                          maxlength="100" placeholder="请输入韩文名称"
                          show-word-limit />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 多语言描述 -->
        <div class="form-section">
          <h3 class="section-title">多语言描述</h3>
          <el-tabs type="border-card">
            <el-tab-pane label="简体中文" name="cn">
              <el-form-item>
                <editor v-model="form.descriptionCn" :min-height="150"/>
              </el-form-item>
            </el-tab-pane>
            <el-tab-pane label="繁体中文" name="hk">
              <el-form-item>
                <editor v-model="form.descriptionHk" :min-height="150"/>
              </el-form-item>
            </el-tab-pane>
            <el-tab-pane label="English" name="en">
              <el-form-item>
                <editor v-model="form.descriptionEn" :min-height="150"/>
              </el-form-item>
            </el-tab-pane>
            <el-tab-pane label="日本語" name="ja">
              <el-form-item>
                <editor v-model="form.descriptionJa" :min-height="150"/>
              </el-form-item>
            </el-tab-pane>
            <el-tab-pane label="한국어" name="kr">
              <el-form-item>
                <editor v-model="form.descriptionKr" :min-height="150"/>
              </el-form-item>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMiningPackage, getMiningPackage, delMiningPackage, addMiningPackage, updateMiningPackage } from "@/api/xms/miningPackage";

export default {
  name: "MiningPackage",
  dicts: ['t_w3_mining_package_type', 't_user_info_is_valid'],
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
      // 基金套餐表格数据
      miningPackageList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        nameCn: null,
        type: null,
        day: null,
        dayRatio: null,
        buyNum: null,
        minBuyPrice: null,
        dailyPenaltyReduction: null,
        penaltyRate: null,
        nameHk: null,
        nameEn: null,
        nameJa: null,
        nameKr: null,
        status: null,
        sort: null,
        descriptionCn: null,
        descriptionHk: null,
        descriptionEn: null,
        descriptionJa: null,
        descriptionKr: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        nameCn: [
          { required: true, message: "基金名称不能为空", trigger: "blur" }
        ],
        nameHk: [
          { required: true, message: "繁体中文不能为空", trigger: "blur" }
        ],
        nameEn: [
          { required: true, message: "英文不能为空", trigger: "blur" }
        ],
        nameJa: [
          { required: true, message: "日文不能为空", trigger: "blur" }
        ],
        nameKr: [
          { required: true, message: "韩文不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "基金类型不能为空", trigger: "change" }
        ],
        buyNum: [
          { required: true, message: "销量不能为空", trigger: "blur" }
        ],
        minBuyPrice: [
          { required: true, message: "最少购买金额限制不能为空", trigger: "blur" }
        ],
        maxBuyPrice: [
          { required: true, message: "最大购买金额限制不能为空", trigger: "blur" }
        ],
        dayRatio: [
          { required: true, message: "日利率不能为空", trigger: "blur" }
        ],
        day: [
          { required: true, message: "有效期天数不能为空", trigger: "blur" }
        ],
        penaltyRate: [
          { required: true, message: "违约金比例不能为空", trigger: "blur" }
        ],
        dailyPenaltyReduction: [
          { required: true, message: "每日递减率不能为空", trigger: "blur" }
        ],
        minPenaltyRate: [
          { required: true, message: "最低违约金比例不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "是否上架不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询基金套餐列表 */
    getList() {
      this.loading = true;
      listMiningPackage(this.queryParams).then(response => {
        this.miningPackageList = response.rows;
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
        nameCn: null,
        type: null,
        day: null,
        dayRatio: null,
        buyNum: null,
        minBuyPrice: null,
        maxBuyPrice: null,
        dailyPenaltyReduction: null,
        penaltyRate: null,
        nameHk: null,
        nameEn: null,
        nameJa: null,
        nameKr: null,
        status: null,
        createTime: null,
        updateTime: null,
        sort: null,
        descriptionCn: null,
        descriptionHk: null,
        descriptionEn: null,
        descriptionJa: null,
        descriptionKr: null
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
      this.title = "添加基金套餐";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getMiningPackage(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改基金套餐";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMiningPackage(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMiningPackage(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除基金套餐编号为"' + ids + '"的数据项？').then(function() {
        return delMiningPackage(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/miningPackage/export', {
        ...this.queryParams
      }, `miningPackage_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

<style scoped>
.form-section {
  margin-bottom: 30px;
}

.section-title {
  margin: 0 0 20px 0;
  padding: 0 0 10px 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  border-bottom: 2px solid #409EFF;
  position: relative;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 30px;
  height: 2px;
  background: #409EFF;
}

/* 调整标签页样式 */
::v-deep .el-tabs--border-card {
  border: 1px solid #DCDFE6;
  border-radius: 4px;
}

::v-deep .el-tabs--border-card .el-tabs__header {
  background-color: #F5F7FA;
  border-bottom: 1px solid #DCDFE6;
  margin: 0;
}

::v-deep .el-tabs--border-card .el-tabs__item {
  border: none;
  color: #909399;
}

::v-deep .el-tabs--border-card .el-tabs__item.is-active {
  color: #409EFF;
  background-color: #FFF;
}

/* 表单间距优化 */
::v-deep .el-form-item {
  margin-bottom: 22px;
}

/* 对话框样式调整 */
::v-deep .el-dialog__body {
  padding: 20px 20px 0 20px;
  max-height: 70vh;
  overflow-y: auto;
}

::v-deep .el-dialog__header {
  padding: 20px 20px 10px 20px;
}

::v-deep .el-dialog__footer {
  padding: 10px 20px 20px 20px;
}
</style>
