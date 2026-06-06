import request from '@/utils/request'

// 查询用户持仓变动流水列表
export function listUserStakePositionFlow(query) {
  return request({
    url: '/xms/userStakePositionFlow/list',
    method: 'get',
    params: query
  })
}

// 查询用户持仓变动流水详细
export function getUserStakePositionFlow(id) {
  return request({
    url: '/xms/userStakePositionFlow/' + id,
    method: 'get'
  })
}

// 新增用户持仓变动流水
export function addUserStakePositionFlow(data) {
  return request({
    url: '/xms/userStakePositionFlow',
    method: 'post',
    data: data
  })
}

// 修改用户持仓变动流水
export function updateUserStakePositionFlow(data) {
  return request({
    url: '/xms/userStakePositionFlow',
    method: 'put',
    data: data
  })
}

// 删除用户持仓变动流水
export function delUserStakePositionFlow(id) {
  return request({
    url: '/xms/userStakePositionFlow/' + id,
    method: 'delete'
  })
}
