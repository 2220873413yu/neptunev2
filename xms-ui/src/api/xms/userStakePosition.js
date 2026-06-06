import request from '@/utils/request'

// 查询用户质押持仓汇总列表
export function listUserStakePosition(query) {
  return request({
    url: '/xms/userStakePosition/list',
    method: 'get',
    params: query
  })
}

// 查询用户质押持仓汇总详细
export function getUserStakePosition(id) {
  return request({
    url: '/xms/userStakePosition/' + id,
    method: 'get'
  })
}

// 新增用户质押持仓汇总
export function addUserStakePosition(data) {
  return request({
    url: '/xms/userStakePosition',
    method: 'post',
    data: data
  })
}

// 修改用户质押持仓汇总
export function updateUserStakePosition(data) {
  return request({
    url: '/xms/userStakePosition',
    method: 'put',
    data: data
  })
}

// 删除用户质押持仓汇总
export function delUserStakePosition(id) {
  return request({
    url: '/xms/userStakePosition/' + id,
    method: 'delete'
  })
}
