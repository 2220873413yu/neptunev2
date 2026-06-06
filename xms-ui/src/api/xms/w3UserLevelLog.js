import request from '@/utils/request'

// 查询用户等级变更记录列表
export function listW3UserLevelLog(query) {
  return request({
    url: '/xms/w3UserLevelLog/list',
    method: 'get',
    params: query
  })
}

// 查询用户等级变更记录详细
export function getW3UserLevelLog(id) {
  return request({
    url: '/xms/w3UserLevelLog/' + id,
    method: 'get'
  })
}

// 新增用户等级变更记录
export function addW3UserLevelLog(data) {
  return request({
    url: '/xms/w3UserLevelLog',
    method: 'post',
    data: data
  })
}

// 修改用户等级变更记录
export function updateW3UserLevelLog(data) {
  return request({
    url: '/xms/w3UserLevelLog',
    method: 'put',
    data: data
  })
}

// 删除用户等级变更记录
export function delW3UserLevelLog(id) {
  return request({
    url: '/xms/w3UserLevelLog/' + id,
    method: 'delete'
  })
}
