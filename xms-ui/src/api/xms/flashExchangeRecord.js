import request from '@/utils/request'

// 查询闪兑记录列表
export function listFlashExchangeRecord(query) {
  return request({
    url: '/xms/flashExchangeRecord/list',
    method: 'get',
    params: query
  })
}

// 查询闪兑记录详细
export function getFlashExchangeRecord(id) {
  return request({
    url: '/xms/flashExchangeRecord/' + id,
    method: 'get'
  })
}

// 新增闪兑记录
export function addFlashExchangeRecord(data) {
  return request({
    url: '/xms/flashExchangeRecord',
    method: 'post',
    data: data
  })
}

// 修改闪兑记录
export function updateFlashExchangeRecord(data) {
  return request({
    url: '/xms/flashExchangeRecord',
    method: 'put',
    data: data
  })
}

// 删除闪兑记录
export function delFlashExchangeRecord(id) {
  return request({
    url: '/xms/flashExchangeRecord/' + id,
    method: 'delete'
  })
}
