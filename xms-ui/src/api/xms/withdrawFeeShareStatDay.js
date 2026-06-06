import request from '@/utils/request'

// 查询提现手续费分红列表
export function listWithdrawFeeShareStatDay(query) {
  return request({
    url: '/xms/withdrawFeeShareStatDay/list',
    method: 'get',
    params: query
  })
}

// 查询提现手续费分红详细
export function getWithdrawFeeShareStatDay(id) {
  return request({
    url: '/xms/withdrawFeeShareStatDay/' + id,
    method: 'get'
  })
}

// 新增提现手续费分红
export function addWithdrawFeeShareStatDay(data) {
  return request({
    url: '/xms/withdrawFeeShareStatDay',
    method: 'post',
    data: data
  })
}

// 修改提现手续费分红
export function updateWithdrawFeeShareStatDay(data) {
  return request({
    url: '/xms/withdrawFeeShareStatDay',
    method: 'put',
    data: data
  })
}

// 删除提现手续费分红
export function delWithdrawFeeShareStatDay(id) {
  return request({
    url: '/xms/withdrawFeeShareStatDay/' + id,
    method: 'delete'
  })
}
