import request from '@/utils/request'

// 查询轮次爆仓判定与执行日志列表
export function listStakeRoundLiquidationLog(query) {
  return request({
    url: '/xms/stakeRoundLiquidationLog/list',
    method: 'get',
    params: query
  })
}

// 查询轮次爆仓判定与执行日志详细
export function getStakeRoundLiquidationLog(id) {
  return request({
    url: '/xms/stakeRoundLiquidationLog/' + id,
    method: 'get'
  })
}

// 新增轮次爆仓判定与执行日志
export function addStakeRoundLiquidationLog(data) {
  return request({
    url: '/xms/stakeRoundLiquidationLog',
    method: 'post',
    data: data
  })
}

// 修改轮次爆仓判定与执行日志
export function updateStakeRoundLiquidationLog(data) {
  return request({
    url: '/xms/stakeRoundLiquidationLog',
    method: 'put',
    data: data
  })
}

// 删除轮次爆仓判定与执行日志
export function delStakeRoundLiquidationLog(id) {
  return request({
    url: '/xms/stakeRoundLiquidationLog/' + id,
    method: 'delete'
  })
}
