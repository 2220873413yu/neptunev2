import request from '@/utils/request'

// 查询币种价格配置列表
export function listCoinPrice(query) {
  return request({
    url: '/xms/coinPrice/list',
    method: 'get',
    params: query
  })
}

// 查询币种价格配置详细
export function getCoinPrice(id) {
  return request({
    url: '/xms/coinPrice/' + id,
    method: 'get'
  })
}

// 新增币种价格配置
export function addCoinPrice(data) {
  return request({
    url: '/xms/coinPrice',
    method: 'post',
    data: data
  })
}

// 修改币种价格配置
export function updateCoinPrice(data) {
  return request({
    url: '/xms/coinPrice',
    method: 'put',
    data: data
  })
}

// 删除币种价格配置
export function delCoinPrice(id) {
  return request({
    url: '/xms/coinPrice/' + id,
    method: 'delete'
  })
}
