import request from '@/utils/request'

// 查询用户等级变动日志列表
export function listUserLevelChangeLog(query) {
  return request({
    url: '/xms/userLevelChangeLog/list',
    method: 'get',
    params: query
  })
}

// 查询用户等级变动日志详细
export function getUserLevelChangeLog(id) {
  return request({
    url: '/xms/userLevelChangeLog/' + id,
    method: 'get'
  })
}

// 新增用户等级变动日志
export function addUserLevelChangeLog(data) {
  return request({
    url: '/xms/userLevelChangeLog',
    method: 'post',
    data: data
  })
}

// 修改用户等级变动日志
export function updateUserLevelChangeLog(data) {
  return request({
    url: '/xms/userLevelChangeLog',
    method: 'put',
    data: data
  })
}

// 删除用户等级变动日志
export function delUserLevelChangeLog(id) {
  return request({
    url: '/xms/userLevelChangeLog/' + id,
    method: 'delete'
  })
}
