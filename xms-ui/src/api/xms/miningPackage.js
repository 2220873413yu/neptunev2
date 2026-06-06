import request from '@/utils/request'

// 查询基金套餐列表
export function listMiningPackage(query) {
  return request({
    url: '/xms/miningPackage/list',
    method: 'get',
    params: query
  })
}

// 查询基金套餐详细
export function getMiningPackage(id) {
  return request({
    url: '/xms/miningPackage/' + id,
    method: 'get'
  })
}

// 新增基金套餐
export function addMiningPackage(data) {
  return request({
    url: '/xms/miningPackage',
    method: 'post',
    data: data
  })
}

// 修改基金套餐
export function updateMiningPackage(data) {
  return request({
    url: '/xms/miningPackage',
    method: 'put',
    data: data
  })
}

// 删除基金套餐
export function delMiningPackage(id) {
  return request({
    url: '/xms/miningPackage/' + id,
    method: 'delete'
  })
}
