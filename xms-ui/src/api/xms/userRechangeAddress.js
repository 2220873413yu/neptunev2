import request from '@/utils/request'

// 查询用户充值地址列表
export function listUserRechangeAddress(query) {
  return request({
    url: '/xms/userRechangeAddress/list',
    method: 'get',
    params: query
  })
}

// 查询用户充值地址详细
export function getUserRechangeAddress(id) {
  return request({
    url: '/xms/userRechangeAddress/' + id,
    method: 'get'
  })
}

// 新增用户充值地址
export function addUserRechangeAddress(data) {
  return request({
    url: '/xms/userRechangeAddress',
    method: 'post',
    data: data
  })
}

// 修改用户充值地址
export function updateUserRechangeAddress(data) {
  return request({
    url: '/xms/userRechangeAddress',
    method: 'put',
    data: data
  })
}

// 删除用户充值地址
export function delUserRechangeAddress(id) {
  return request({
    url: '/xms/userRechangeAddress/' + id,
    method: 'delete'
  })
}
