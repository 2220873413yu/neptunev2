import request from '@/utils/request'

// 查询用户信息快照列表
export function listUserInfoSnapshot(query) {
  return request({
    url: '/xms/userInfoSnapshot/list',
    method: 'get',
    params: query
  })
}

// 查询用户信息快照详细
export function getUserInfoSnapshot(snapshotId) {
  return request({
    url: '/xms/userInfoSnapshot/' + snapshotId,
    method: 'get'
  })
}

// 新增用户信息快照
export function addUserInfoSnapshot(data) {
  return request({
    url: '/xms/userInfoSnapshot',
    method: 'post',
    data: data
  })
}

// 修改用户信息快照
export function updateUserInfoSnapshot(data) {
  return request({
    url: '/xms/userInfoSnapshot',
    method: 'put',
    data: data
  })
}

// 删除用户信息快照
export function delUserInfoSnapshot(snapshotId) {
  return request({
    url: '/xms/userInfoSnapshot/' + snapshotId,
    method: 'delete'
  })
}
