import request from '@/utils/request'

// 查询基金订单列表
export function listMiningPackageOrder(query) {
  return request({
    url: '/xms/miningPackageOrder/list',
    method: 'get',
    params: query
  })
}


// 查询固定矿机基金套餐列表
export function listMiningPackageList(query) {
  return request({
    url: '/xms/miningPackageOrder/miningPackageList',
    method: 'get',
    params: query
  })
}

// 查询订单天数列表
export function doGetDistinctDays() {
  return request({
    url: '/xms/miningPackageOrder/getDistinctDays',
    method: 'get'
  })
}

// 查询基金订单详细
export function getMiningPackageOrder(id) {
  return request({
    url: '/xms/miningPackageOrder/' + id,
    method: 'get'
  })
}

// 新增基金订单
export function addMiningPackageOrder(data) {
  return request({
    url: '/xms/miningPackageOrder',
    method: 'post',
    data: data
  })
}

// 修改基金订单
export function updateMiningPackageOrder(data) {
  return request({
    url: '/xms/miningPackageOrder',
    method: 'put',
    data: data
  })
}

// 删除基金订单
export function delMiningPackageOrder(id) {
  return request({
    url: '/xms/miningPackageOrder/' + id,
    method: 'delete'
  })
}
