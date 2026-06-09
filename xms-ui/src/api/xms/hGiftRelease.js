import request from '@/utils/request'

// 查询H赠送释放列表
export function listHGiftRelease(query) {
  return request({
    url: '/xms/hGiftRelease/list',
    method: 'get',
    params: query
  })
}

// 查询H赠送释放详细
export function getHGiftRelease(id) {
  return request({
    url: '/xms/hGiftRelease/' + id,
    method: 'get'
  })
}

// 新增H赠送释放
export function addHGiftRelease(data) {
  return request({
    url: '/xms/hGiftRelease',
    method: 'post',
    data: data
  })
}

// 冻结H赠送释放
export function freezeHGiftRelease(id) {
  return request({
    url: '/xms/hGiftRelease/freeze/' + id,
    method: 'put'
  })
}

// 解冻H赠送释放
export function unfreezeHGiftRelease(id) {
  return request({
    url: '/xms/hGiftRelease/unfreeze/' + id,
    method: 'put'
  })
}
