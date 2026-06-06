import request from '@/utils/request'

// 查询币种图标配置列表
export function listMarketsCoin(query) {
  return request({
    url: '/xms/marketsCoin/list',
    method: 'get',
    params: query
  })
}

// 查询币种图标配置详细
export function getMarketsCoin(id) {
  return request({
    url: '/xms/marketsCoin/' + id,
    method: 'get'
  })
}

// 新增币种图标配置
export function addMarketsCoin(data) {
  return request({
    url: '/xms/marketsCoin',
    method: 'post',
    data: data
  })
}

// 修改币种图标配置
export function updateMarketsCoin(data) {
  return request({
    url: '/xms/marketsCoin',
    method: 'put',
    data: data
  })
}

// 删除币种图标配置
export function delMarketsCoin(id) {
  return request({
    url: '/xms/marketsCoin/' + id,
    method: 'delete'
  })
}
