import request from '@/utils/request'

// 查询用户收益信息列表
export function listUserIncomeSummary(query) {
  return request({
    url: '/xms/userIncomeSummary/list',
    method: 'get',
    params: query
  })
}

// 查询用户收益信息详细
export function getUserIncomeSummary(userId) {
  return request({
    url: '/xms/userIncomeSummary/' + userId,
    method: 'get'
  })
}

// 新增用户收益信息
export function addUserIncomeSummary(data) {
  return request({
    url: '/xms/userIncomeSummary',
    method: 'post',
    data: data
  })
}

// 修改用户收益信息
export function updateUserIncomeSummary(data) {
  return request({
    url: '/xms/userIncomeSummary',
    method: 'put',
    data: data
  })
}

// 删除用户收益信息
export function delUserIncomeSummary(userId) {
  return request({
    url: '/xms/userIncomeSummary/' + userId,
    method: 'delete'
  })
}
