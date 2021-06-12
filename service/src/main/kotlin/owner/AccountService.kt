package settlement.kotlin.service.owner

import settlement.kotlin.db.owner.AccountEntity
import settlement.kotlin.db.owner.AccountRepository
import settlement.kotlin.db.owner.OwnerRepository
import settlement.kotlin.service.owner.model.dto.AccountDto
import settlement.kotlin.service.owner.model.info.CreateAccountInfo

class AccountService(
    private val accountRepository: AccountRepository,
    private val ownerRepository: OwnerRepository
) {
    fun createAccount(req: CreateAccountInfo): AccountDto {
        val owner = ownerRepository.findById(req.ownerId).orElseThrow(::RuntimeException)

        accountRepository.findByOwnerIdAndBankAndBankAccount(
            ownerId = owner.id,
            bank = req.bank,
            bankAccount = req.bankAccount
        )?.run {
            throw RuntimeException()
        }

        return accountRepository.save(
            AccountEntity(
                owner = owner,
                bank = req.bank,
                bankAccount = req.bankAccount,
                accountHolder = req.accountHolder
            )
        ).let {
            AccountDto(
                id = it.id,
                ownerId = it.owner.id,
                bank = it.bank,
                bankAccount = it.bankAccount,
                accountHolder = it.accountHolder
            )
        }
    }
}
