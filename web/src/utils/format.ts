export function formatCurrency(value: number): string {
  return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value)
}

export function orderTotal(order: {
  productSnapshots: Array<{ price: number; specOptionSnapshots: Array<{ extraPrice: number }> }>
}): number {
  return order.productSnapshots.reduce((sum, item) => {
    const options = item.specOptionSnapshots.reduce((acc, opt) => acc + (opt.extraPrice ?? 0), 0)
    return sum + item.price + options
  }, 0)
}

export const ORDER_STATUS_LABEL: Record<string, string> = {
  EM_CADASTRAMENTO: 'Em cadastramento',
  AGUARDANDO_PAGAMENTO: 'Aguardando pagamento',
  AGUARDANDO_RESTAURANTE: 'Aguardando restaurante',
  PREPARANDO: 'Preparando',
  SAIU_PARA_ENTREGA: 'Saiu para entrega',
  ENTREGADOR_NO_LOCAL: 'Entregador no local',
  ENTREGUE: 'Entregue',
  CANCELADO: 'Cancelado',
}

export const OWNER_ORDER_STATUSES = [
  'AGUARDANDO_RESTAURANTE',
  'PREPARANDO',
  'SAIU_PARA_ENTREGA',
  'ENTREGADOR_NO_LOCAL',
  'ENTREGUE',
] as const
