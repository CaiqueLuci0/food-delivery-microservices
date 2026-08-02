import { httpClient } from '@/api/httpClient'
import type { Payment } from '@/types/api'

export const paymentService = {
  markAsPaid: async (orderId: string): Promise<Payment> => {
    const { data } = await httpClient.patch<Payment>(`/payment-ms/payments/${orderId}/pay`)
    return data
  },
}
