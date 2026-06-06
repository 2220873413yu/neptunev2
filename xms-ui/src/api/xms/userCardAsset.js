import request from '@/utils/request'

// 查询卡片持有信息列表
export function listUserCardAsset(query) {
  return request({
    url: '/xms/userCardAsset/list',
    method: 'get',
    params: query
  })
}

// 查询卡片持有信息详细
export function getUserCardAsset(id) {
  return request({
    url: '/xms/userCardAsset/' + id,
    method: 'get'
  })
}

// 新增卡片持有信息
export function addUserCardAsset(data) {
  return request({
    url: '/xms/userCardAsset',
    method: 'post',
    data: data
  })
}

// 修改卡片持有信息
export function updateUserCardAsset(data) {
  return request({
    url: '/xms/userCardAsset',
    method: 'put',
    data: data
  })
}

// 删除卡片持有信息
export function delUserCardAsset(id) {
  return request({
    url: '/xms/userCardAsset/' + id,
    method: 'delete'
  })
}
