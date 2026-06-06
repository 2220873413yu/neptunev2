import request from '@/utils/request'

// 查询财富仓阶段解锁配置列表
export function listWealthVaultStageConfig(query) {
  return request({
    url: '/xms/wealthVaultStageConfig/list',
    method: 'get',
    params: query
  })
}

// 查询财富仓阶段解锁配置详细
export function getWealthVaultStageConfig(id) {
  return request({
    url: '/xms/wealthVaultStageConfig/' + id,
    method: 'get'
  })
}

// 新增财富仓阶段解锁配置
export function addWealthVaultStageConfig(data) {
  return request({
    url: '/xms/wealthVaultStageConfig',
    method: 'post',
    data: data
  })
}

// 修改财富仓阶段解锁配置
export function updateWealthVaultStageConfig(data) {
  return request({
    url: '/xms/wealthVaultStageConfig',
    method: 'put',
    data: data
  })
}

// 删除财富仓阶段解锁配置
export function delWealthVaultStageConfig(id) {
  return request({
    url: '/xms/wealthVaultStageConfig/' + id,
    method: 'delete'
  })
}
