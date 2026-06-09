import request from '@/utils/request'

// 查询旧H换ACP入金记录列表
export function listOldHToAcpDepositRecord(query) {
  return request({
    url: '/xms/oldHToAcpDepositRecord/list',
    method: 'get',
    params: query
  })
}
