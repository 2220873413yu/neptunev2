<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="轮次ID" prop="stakeRoundId" label-width="120px">
        <el-input
          v-model="queryParams.stakeRoundId"
          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"
          placeholder="请输入轮次ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="用户ID" prop="userId" label-width="120px">
        <el-input
          v-model="queryParams.userId"
          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"

          placeholder="请输入用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="钱包地址" prop="account" label-width="120px">
        <el-input
          v-model="queryParams.account"
          placeholder="请输入钱包地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="结算用户等级" label-width="120px" prop="finaGameLevel">
        <el-select v-model="queryParams.finaGameLevel" clearable placeholder="请选择">
          <el-option
            v-for="dict in dict.type.t_user_info_game_level"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="结算节点等级" label-width="120px" prop="finaNodeLevel">
        <el-select v-model="queryParams.finaNodeLevel" clearable placeholder="请选择">
          <el-option
            v-for="dict in dict.type.t_node_plan_node_level"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="结算层级等级" label-width="120px" prop="finaLayerLevel">
        <el-select v-model="queryParams.finaLayerLevel" clearable placeholder="请选择">
          <el-option
            v-for="dict in dict.type.t_user_invest_layer_config_level"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="用户等级" label-width="120px" prop="gameLevel">
        <el-select v-model="queryParams.gameLevel" clearable placeholder="请选择">
          <el-option
            v-for="dict in dict.type.t_user_info_game_level"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="节点等级" label-width="120px" prop="nodeLevel">
        <el-select v-model="queryParams.nodeLevel" clearable placeholder="请选择">
          <el-option
            v-for="dict in dict.type.t_node_plan_node_level"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="层级等级" label-width="120px" prop="layerLevel">
        <el-select v-model="queryParams.layerLevel" clearable placeholder="请选择">
          <el-option
            v-for="dict in dict.type.t_user_invest_layer_config_level"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>




      <el-form-item label="虚拟用户等级" label-width="120px" prop="minGameLevel">
        <el-select v-model="queryParams.minGameLevel" clearable placeholder="请选择">
          <el-option
            v-for="dict in dict.type.t_user_info_game_level"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="虚拟节点等级" label-width="120px" prop="minNodeLevel">
        <el-select v-model="queryParams.minNodeLevel" clearable placeholder="请选择">
          <el-option
            v-for="dict in dict.type.t_node_plan_node_level"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="虚拟层级等级" label-width="120px" prop="minLayerLevel">
        <el-select v-model="queryParams.minLayerLevel" clearable placeholder="请选择">
          <el-option
            v-for="dict in dict.type.t_user_invest_layer_config_level"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="邀请用户ID" prop="inviteUserId" label-width="120px">
        <el-input
          v-model="queryParams.inviteUserId"
          oninput="if(isNaN(value)) { value = null } else { value = value.replace('.', '') }"

          placeholder="请输入邀请用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
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
          v-hasPermi="['xms:userInfoSnapshot:add']"
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
          v-hasPermi="['xms:userInfoSnapshot:edit']"
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
          v-hasPermi="['xms:userInfoSnapshot:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['xms:userInfoSnapshot:export']"
        >导出</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="userInfoSnapshotList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="snapshotId" />
      <el-table-column label="轮次ID" align="center" prop="stakeRoundId" />

      <el-table-column align="center" label="用户ID" prop="userId"/>
      <el-table-column align="center" label="用户编码" prop="userCode" />
      <!--      <el-table-column label="是否有效" align="center" prop="isValid" width="80">
              <template slot-scope="scope">
                <dict-tag :options="dict.type.t_user_info_is_valid" :value="scope.row.isValid"/>
              </template>
            </el-table-column>-->
      <!--      <el-table-column label="昵称" align="center" prop="nickName" />
            <el-table-column label="头像" align="center" prop="avatar" width="100">
              <template slot-scope="scope">
                <image-preview :src="scope.row.avatar" :width="50" :height="50"/>
              </template>
            </el-table-column>
            <el-table-column label="openId" align="center" prop="juOpenId" show-overflow-tooltip width="150"/>-->
      <el-table-column align="center" label="钱包地址" prop="account" width="150"/>

      <el-table-column align="center" label="邀请用户信息" prop="inviteUserCode" width="180">
        <template slot-scope="scope">
          <div class="exchange-info" style="text-align: left;">
            用户编码: {{scope.row.inviteUserCode}}<br/>
            用户ID: {{scope.row.inviteUserId}}<br/>
          </div>
        </template>
      </el-table-column>

      <el-table-column align="center" label="节点信息" width="180">
        <template slot-scope="scope">
          <div style="text-align: left;">
            <div style="display: flex; align-items: center; flex-wrap: nowrap; gap: 4px; margin-bottom: 6px;">
              <span>真实等级:</span>
              <dict-tag :options="dict.type.t_node_plan_node_level" :value="scope.row.nodeLevel"/>
            </div>
            <div style="display: flex; align-items: center; flex-wrap: nowrap; gap: 4px;">
              <span>虚拟等级:</span>
              <dict-tag :options="dict.type.t_node_plan_node_level" :value="scope.row.minNodeLevel"/>
            </div>
          </div>
        </template>
      </el-table-column>

      <el-table-column align="center" label="等级信息" width="180">
        <template slot-scope="scope">
          <div style="text-align: left;">
            <div style="display: flex; align-items: center; flex-wrap: nowrap; gap: 4px; margin-bottom: 6px;">
              <span>真实等级:</span>
              <dict-tag :options="dict.type.t_user_info_game_level" :value="scope.row.gameLevel"/>
            </div>
            <div style="display: flex; align-items: center; flex-wrap: nowrap; gap: 4px;">
              <span>虚拟等级:</span>
              <dict-tag :options="dict.type.t_user_info_game_level" :value="scope.row.minGameLevel"/>
            </div>
          </div>
        </template>
      </el-table-column>

      <el-table-column align="center" label="层级等级" width="180">
        <template slot-scope="scope">
          <div style="text-align: left;">
            <div style="display: flex; align-items: center; flex-wrap: nowrap; gap: 4px; margin-bottom: 6px;">
              <span>真实等级:</span>
              <dict-tag :options="dict.type.t_user_invest_layer_config_level" :value="scope.row.layerLevel"/>
            </div>
            <div style="display: flex; align-items: center; flex-wrap: nowrap; gap: 4px;">
              <span>虚拟等级:</span>
              <dict-tag :options="dict.type.t_user_invest_layer_config_level" :value="scope.row.minLayerLevel"/>
            </div>
          </div>
        </template>
      </el-table-column>

      <el-table-column align="center" label="是否工作室补贴" prop="hasStudioSubsidyEligible">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_info_is_valid" :value="scope.row.hasStudioSubsidyEligible"/>
        </template>
      </el-table-column>


      <!--
            -->
      <!--      <el-table-column label="今日新增业绩" align="center" prop="todayPerformance" width="150"/>
            <el-table-column label="团队提现总额" align="center" prop="teamWithdrawBalance" width="150"/>-->
      <!--      <el-table-column label="今日新增" align="center" width="150">
              <template slot-scope="scope">
                <div class="exchange-info" style="text-align: left;">
                  个人业绩: {{ scope.row.todayPerformance }}<br>
                  团队人数: {{ scope.row.todayPerformance }}<br>
                  团队业绩: {{ scope.row.todayTeamPerformance }}<br>
                </div>
              </template>
            </el-table-column>-->
      <!--      <el-table-column label="小区业绩" align="center" prop="communityPerformance" />-->
      <!--      <el-table-column label="未释放数量信息" align="center" width="150">
              <template slot-scope="scope">
                <div class="exchange-info" style="text-align: left;">
                  个人: {{ scope.row.userRemainAmount }}<br>
                  团队: {{ scope.row.teamRemainAmount }} <br>
                </div>
              </template>
            </el-table-column>-->
      <!--      <el-table-column label="提现信息(BOOMAI)" align="center" width="150">
              <template slot-scope="scope">
                <div class="exchange-info" style="text-align: left;">
                  团队提现: {{ scope.row.teamWithdrawBalance }}<br>
                  我的提现: {{ scope.row.withdrawalBalance }} <br>
                </div>
              </template>
            </el-table-column>-->

      <!--      <el-table-column label="今日我的销毁" align="center" width="150">
              <template slot-scope="scope">
                <div class="exchange-info" style="text-align: left;">
                  {{ scope.row.todayPerformance }} USDT<br>
                  {{ scope.row.todayPerformanceV1 }} BOOMAI<br>
                </div>
              </template>
            </el-table-column>-->

      <!--      <el-table-column label="今日团销毁" align="center" width="150">
              <template slot-scope="scope">
                <div class="exchange-info" style="text-align: left;">
                  {{ scope.row.todayUmbrellaPerformance }} USDT<br>
                  {{ scope.row.todayUmbrellaPerformanceV1 }} BOOMAI<br>
                </div>
              </template>
            </el-table-column>-->


      <el-table-column align="center" label="节点相关" width="150">
        <template slot-scope="scope">
          <div class="exchange-info" style="text-align: left;">
            直推节点: {{ scope.row.subPerformance }} <br>
            团队节点: {{ scope.row.umbrellaPerformance }} <br>
          </div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="团队相关"width="150">
        <template slot-scope="scope">
          <div class="exchange-info" style="text-align: left;">
            直推用户数: {{ scope.row.subNum }}<br>
            团队用户数: {{ scope.row.umbrellaNum }}<br>
          </div>
        </template>
      </el-table-column>

      <!--      <el-table-column label="团队相关" align="center"width="150">
              <template slot-scope="scope">
                <div class="exchange-info" style="text-align: left;">
                  直推有效用户数: {{ scope.row.validSubNum }}<br>
                  团队有效用户数: {{ scope.row.validUmbrellaNum }}<br>
                </div>
              </template>
            </el-table-column>-->

      <!--      <el-table-column label="提现信息" align="center"width="150">
              <template slot-scope="scope">
                <div class="exchange-info" style="text-align: left;">
                  个人提现: {{ scope.row.withdrawalBalance }}<br>
                  团队提现: {{ scope.row.teamWithdrawBalance }}<br>
                </div>
              </template>
            </el-table-column>-->

      <el-table-column align="center" label="账号状态" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.t_user_info_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="快照时间" align="center" prop="snapshotTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.snapshotTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="是否工作室补贴 0:否,1:是(废弃)" align="center" prop="hasStudioSubsidyEligible" />-->
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['xms:userInfoSnapshot:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['xms:userInfoSnapshot:remove']"
          >删除</el-button>
        </template>
      </el-table-column>-->
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改用户信息快照对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="轮次id" prop="stakeRoundId">
          <el-input v-model="form.stakeRoundId" placeholder="请输入轮次id" />
        </el-form-item>
        <el-form-item label="快照时间" prop="snapshotTime">
          <el-date-picker clearable
            v-model="form.snapshotTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择快照时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="用户id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户id" />
        </el-form-item>
        <el-form-item label="钱包地址" prop="account">
          <el-input v-model="form.account" placeholder="请输入钱包地址" />
        </el-form-item>
        <el-form-item label="用户编码" prop="userCode">
          <el-input v-model="form.userCode" placeholder="请输入用户编码" />
        </el-form-item>
        <el-form-item label="头像" prop="avatar">
          <el-input v-model="form.avatar" placeholder="请输入头像" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="节点等级 0:无,1:普通节点,2:中级节点,3:高级节点,4:超级节点" prop="nodeLevel">
          <el-input v-model="form.nodeLevel" placeholder="请输入节点等级 0:无,1:普通节点,2:中级节点,3:高级节点,4:超级节点" />
        </el-form-item>
        <el-form-item label="保底的节点等级" prop="minNodeLevel">
          <el-input v-model="form.minNodeLevel" placeholder="请输入保底的节点等级" />
        </el-form-item>
        <el-form-item label="层级等级 考核层级奖" prop="layerLevel">
          <el-input v-model="form.layerLevel" placeholder="请输入层级等级 考核层级奖" />
        </el-form-item>
        <el-form-item label="保底的层级等级" prop="minLayerLevel">
          <el-input v-model="form.minLayerLevel" placeholder="请输入保底的层级等级" />
        </el-form-item>
        <el-form-item label="等级(0.无 1.S1 2.S2 3.S3 4.S4 5.S5 6.S6,7.S7,8.S8)" prop="gameLevel">
          <el-input v-model="form.gameLevel" placeholder="请输入等级(0.无 1.S1 2.S2 3.S3 4.S4 5.S5 6.S6,7.S7,8.S8)" />
        </el-form-item>
        <el-form-item label="保底等级" prop="minGameLevel">
          <el-input v-model="form.minGameLevel" placeholder="请输入保底等级" />
        </el-form-item>
        <el-form-item label="邀请用户编码" prop="inviteUserCode">
          <el-input v-model="form.inviteUserCode" placeholder="请输入邀请用户编码" />
        </el-form-item>
        <el-form-item label="邀请用户id" prop="inviteUserId">
          <el-input v-model="form.inviteUserId" placeholder="请输入邀请用户id" />
        </el-form-item>
        <el-form-item label="是否有效用户(0.否 1.是)" prop="isValid">
          <el-input v-model="form.isValid" placeholder="请输入是否有效用户(0.否 1.是)" />
        </el-form-item>
        <el-form-item label="直推用户数" prop="subNum">
          <el-input v-model="form.subNum" placeholder="请输入直推用户数" />
        </el-form-item>
        <el-form-item label="直推有效用户数" prop="validSubNum">
          <el-input v-model="form.validSubNum" placeholder="请输入直推有效用户数" />
        </el-form-item>
        <el-form-item label="团队用户数" prop="umbrellaNum">
          <el-input v-model="form.umbrellaNum" placeholder="请输入团队用户数" />
        </el-form-item>
        <el-form-item label="团队有效用户数" prop="validUmbrellaNum">
          <el-input v-model="form.validUmbrellaNum" placeholder="请输入团队有效用户数" />
        </el-form-item>
        <el-form-item label="我的业绩(质押量)" prop="performance">
          <el-input v-model="form.performance" placeholder="请输入我的业绩(质押量)" />
        </el-form-item>
        <el-form-item label="直推业绩(矿机)" prop="subMining">
          <el-input v-model="form.subMining" placeholder="请输入直推业绩(矿机)" />
        </el-form-item>
        <el-form-item label="团队业绩(矿机)" prop="performanceMining">
          <el-input v-model="form.performanceMining" placeholder="请输入团队业绩(矿机)" />
        </el-form-item>
        <el-form-item label="小区业绩(质押量)" prop="communityPerformance">
          <el-input v-model="form.communityPerformance" placeholder="请输入小区业绩(质押量)" />
        </el-form-item>
        <el-form-item label="直推业绩(质押量)" prop="subPerformance">
          <el-input v-model="form.subPerformance" placeholder="请输入直推业绩(质押量)" />
        </el-form-item>
        <el-form-item label="团队业绩(质押量)" prop="umbrellaPerformance">
          <el-input v-model="form.umbrellaPerformance" placeholder="请输入团队业绩(质押量)" />
        </el-form-item>
        <el-form-item label="父级链" prop="parentChain">
          <el-input v-model="form.parentChain" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="USDT 提现开关(1.关 2.开)" prop="withdrawalOpenOrClose">
          <el-input v-model="form.withdrawalOpenOrClose" placeholder="请输入USDT 提现开关(1.关 2.开)" />
        </el-form-item>
        <el-form-item label="最近登录的ip地址" prop="lastLoginIp">
          <el-input v-model="form.lastLoginIp" placeholder="请输入最近登录的ip地址" />
        </el-form-item>
        <el-form-item label="删除标记,默认0,1:已删除" prop="deleted">
          <el-input v-model="form.deleted" placeholder="请输入删除标记,默认0,1:已删除" />
        </el-form-item>
        <el-form-item label="可分红数量" prop="dividendAvailableAmount">
          <el-input v-model="form.dividendAvailableAmount" placeholder="请输入可分红数量" />
        </el-form-item>
        <el-form-item label="已分红数量" prop="distributedAmount">
          <el-input v-model="form.distributedAmount" placeholder="请输入已分红数量" />
        </el-form-item>
        <el-form-item label="是否工作室补贴 0:否,1:是(废弃)" prop="hasStudioSubsidyEligible">
          <el-input v-model="form.hasStudioSubsidyEligible" placeholder="请输入是否工作室补贴 0:否,1:是(废弃)" />
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
import { listUserInfoSnapshot, getUserInfoSnapshot, delUserInfoSnapshot, addUserInfoSnapshot, updateUserInfoSnapshot } from "@/api/xms/userInfoSnapshot";

export default {
  name: "UserInfoSnapshot",
  dicts: ['t_user_info_game_level','t_node_plan_node_level','t_user_invest_layer_config_level',
    'biz_open_or_close','t_user_info_is_valid', 't_user_info_status','t_w3_community_subsidy_level'],
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
      // 用户信息快照表格数据
      userInfoSnapshotList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否工作室补贴 0:否,1:是(废弃)时间范围
      daterangeCreateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        stakeRoundId: null,

        finaNodeLevel: null,
        finaLayerLevel: null,
        finaGameLevel: null,

        minGameLevel: null,
        minLayerLevel: null,
        minNodeLevel: null,

        gameLevel: null,
        layerLevel: null,
        nodeLevel: null,

        autoMining: null,
        userCode: null,
        autoWithdrawal: null,
        autoTransfer: null,
        account: null,
        mnemonic: null,
        partnerStatus: null,
        withdrawalOpenOrClose: null,
        nodeIdentity: null,
        recAddress: null,


        isValid: null,
        loginPwd: null,
        loginSalt: null,
        email: null,
        payPwd: null,
        paySalt: null,
        inviteUserCode: null,
        status: null,
        subNum: null,
        umbrellaNum: null,
        performance: null,
        umbrellaPerformance: null,
        activeFlag: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        stakeRoundId: [
          { required: true, message: "轮次id不能为空", trigger: "blur" }
        ],
        snapshotTime: [
          { required: true, message: "快照时间不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "用户id不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户信息快照列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listUserInfoSnapshot(this.queryParams).then(response => {
        this.userInfoSnapshotList = response.rows;
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
        snapshotId: null,
        stakeRoundId: null,
        snapshotTime: null,
        userId: null,
        account: null,
        userCode: null,
        avatar: null,
        email: null,
        nodeLevel: null,
        minNodeLevel: null,
        layerLevel: null,
        minLayerLevel: null,
        gameLevel: null,
        minGameLevel: null,
        inviteUserCode: null,
        inviteUserId: null,
        status: null,
        isValid: null,
        subNum: null,
        validSubNum: null,
        umbrellaNum: null,
        validUmbrellaNum: null,
        performance: null,
        subMining: null,
        performanceMining: null,
        communityPerformance: null,
        subPerformance: null,
        umbrellaPerformance: null,
        parentChain: null,
        createTime: null,
        updateTime: null,
        withdrawalOpenOrClose: null,
        lastLoginIp: null,
        deleted: null,
        dividendAvailableAmount: null,
        distributedAmount: null,
        hasStudioSubsidyEligible: null
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
      this.ids = selection.map(item => item.snapshotId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加用户信息快照";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const snapshotId = row.snapshotId || this.ids
      getUserInfoSnapshot(snapshotId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户信息快照";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.snapshotId != null) {
            updateUserInfoSnapshot(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUserInfoSnapshot(this.form).then(response => {
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
      const snapshotIds = row.snapshotId || this.ids;
      this.$modal.confirm('是否确认删除用户信息快照编号为"' + snapshotIds + '"的数据项？').then(function() {
        return delUserInfoSnapshot(snapshotIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('xms/userInfoSnapshot/export', {
        ...this.queryParams
      }, `userInfoSnapshot_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
