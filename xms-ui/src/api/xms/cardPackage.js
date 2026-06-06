import request from '@/utils/request'

// 查询卡片套餐列表
export function listCardPackage(query) {
  return request({
    url: '/xms/cardPackage/list',
    method: 'get',
    params: query
  })
}

// 查询卡片套餐详细
export function getCardPackage(id) {
  return request({
    url: '/xms/cardPackage/' + id,
    method: 'get'
  })
}

// 新增卡片套餐
export function addCardPackage(data) {
  return request({
    url: '/xms/cardPackage',
    method: 'post',
    data: data
  })
}

// 修改卡片套餐
export function updateCardPackage(data) {
  return request({
    url: '/xms/cardPackage',
    method: 'put',
    data: data
  })
}

// 删除卡片套餐
export function delCardPackage(id) {
  return request({
    url: '/xms/cardPackage/' + id,
    method: 'delete'
  })
}
