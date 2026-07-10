import request from '@/utils/request'

// 查询全局质押轮次列表
export function listStakeRound(query) {
  return request({
    url: '/xms/stakeRound/list',
    method: 'get',
    params: query
  })
}

// 查询全局质押轮次详细
export function getStakeRound(id) {
  return request({
    url: '/xms/stakeRound/' + id,
    method: 'get'
  })
}

// 新增全局质押轮次
export function addStakeRound(data) {
  return request({
    url: '/xms/stakeRound',
    method: 'post',
    data: data
  })
}

// 修改全局质押轮次
export function updateStakeRound(data) {
  return request({
    url: '/xms/stakeRound',
    method: 'put',
    data: data
  })
}

// 修改爆仓检测开关
export function updateLiquidationCheckSwitch(data) {
  return request({
    url: '/xms/stakeRound/liquidationCheckSwitch',
    method: 'put',
    data: data
  })
}

// 删除全局质押轮次
export function delStakeRound(id) {
  return request({
    url: '/xms/stakeRound/' + id,
    method: 'delete'
  })
}
