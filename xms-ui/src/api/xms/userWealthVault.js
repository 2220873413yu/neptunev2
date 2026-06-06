import request from '@/utils/request'

// 查询用户财富表列表
export function listUserWealthVault(query) {
  return request({
    url: '/xms/userWealthVault/list',
    method: 'get',
    params: query
  })
}

// 查询用户财富表详细
export function getUserWealthVault(id) {
  return request({
    url: '/xms/userWealthVault/' + id,
    method: 'get'
  })
}

// 新增用户财富表
export function addUserWealthVault(data) {
  return request({
    url: '/xms/userWealthVault',
    method: 'post',
    data: data
  })
}

// 修改用户财富表
export function updateUserWealthVault(data) {
  return request({
    url: '/xms/userWealthVault',
    method: 'put',
    data: data
  })
}

// 删除用户财富表
export function delUserWealthVault(id) {
  return request({
    url: '/xms/userWealthVault/' + id,
    method: 'delete'
  })
}
