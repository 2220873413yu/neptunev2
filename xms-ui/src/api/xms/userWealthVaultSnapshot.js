import request from '@/utils/request'

// 查询用户财富仓快照列表
export function listUserWealthVaultSnapshot(query) {
  return request({
    url: '/xms/userWealthVaultSnapshot/list',
    method: 'get',
    params: query
  })
}

// 查询用户财富仓快照详细
export function getUserWealthVaultSnapshot(snapshotId) {
  return request({
    url: '/xms/userWealthVaultSnapshot/' + snapshotId,
    method: 'get'
  })
}

// 新增用户财富仓快照
export function addUserWealthVaultSnapshot(data) {
  return request({
    url: '/xms/userWealthVaultSnapshot',
    method: 'post',
    data: data
  })
}

// 修改用户财富仓快照
export function updateUserWealthVaultSnapshot(data) {
  return request({
    url: '/xms/userWealthVaultSnapshot',
    method: 'put',
    data: data
  })
}

// 删除用户财富仓快照
export function delUserWealthVaultSnapshot(snapshotId) {
  return request({
    url: '/xms/userWealthVaultSnapshot/' + snapshotId,
    method: 'delete'
  })
}
