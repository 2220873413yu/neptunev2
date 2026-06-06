import request from '@/utils/request'

// 查询用户余额快照列表
export function listUserMoneySnapshot(query) {
  return request({
    url: '/xms/userMoneySnapshot/list',
    method: 'get',
    params: query
  })
}

// 查询用户余额快照详细
export function getUserMoneySnapshot(snapshotId) {
  return request({
    url: '/xms/userMoneySnapshot/' + snapshotId,
    method: 'get'
  })
}

// 新增用户余额快照
export function addUserMoneySnapshot(data) {
  return request({
    url: '/xms/userMoneySnapshot',
    method: 'post',
    data: data
  })
}

// 修改用户余额快照
export function updateUserMoneySnapshot(data) {
  return request({
    url: '/xms/userMoneySnapshot',
    method: 'put',
    data: data
  })
}

// 删除用户余额快照
export function delUserMoneySnapshot(snapshotId) {
  return request({
    url: '/xms/userMoneySnapshot/' + snapshotId,
    method: 'delete'
  })
}
