import request from '@/utils/request'

// 查询用户财富仓流水列表
export function listUserWealthVaultFlow(query) {
  return request({
    url: '/xms/userWealthVaultFlow/list',
    method: 'get',
    params: query
  })
}

// 查询用户财富仓流水详细
export function getUserWealthVaultFlow(id) {
  return request({
    url: '/xms/userWealthVaultFlow/' + id,
    method: 'get'
  })
}

// 新增用户财富仓流水
export function addUserWealthVaultFlow(data) {
  return request({
    url: '/xms/userWealthVaultFlow',
    method: 'post',
    data: data
  })
}

// 修改用户财富仓流水
export function updateUserWealthVaultFlow(data) {
  return request({
    url: '/xms/userWealthVaultFlow',
    method: 'put',
    data: data
  })
}

// 删除用户财富仓流水
export function delUserWealthVaultFlow(id) {
  return request({
    url: '/xms/userWealthVaultFlow/' + id,
    method: 'delete'
  })
}
