package settlement.kotlin.service.owner

import org.springframework.transaction.annotation.Transactional
import settlement.kotlin.db.owner.Account
import settlement.kotlin.db.owner.AccountRepository
import settlement.kotlin.db.owner.OwnerRepository
import settlement.kotlin.service.owner.req.CreateAccountRequest
import settlement.kotlin.service.owner.req.ModifyAccountRequest
import settlement.kotlin.service.owner.res.CreateAccountResponse
import settlement.kotlin.service.owner.res.ModifyAccountResponse

class AccountService(
    private val accountRepository: AccountRepository,
    private val ownerRepository: OwnerRepository
) {
    fun createAccount(req: CreateAccountRequest): CreateAccountResponse {
        val owner = ownerRepository.findById(req.ownerId).orElseThrow(::RuntimeException)

        accountRepository.findByOwnerIdAndBankAndBankAccount(
            ownerId = owner.id,
            bank = req.bank,
            bankAccount = req.bankAccount
        )?.run {
            throw RuntimeException()
        }

        return accountRepository.save(
            Account(
                owner = owner,
                bank = req.bank,
                bankAccount = req.bankAccount,
                accountHolder = req.accountHolder
            )
        ).let {
            CreateAccountResponse(
                id = it.id,
                ownerId = it.owner.id,
                bank = it.bank,
                bankAccount = it.bankAccount,
                accountHolder = it.accountHolder
            )
        }
    }

    fun modifyAccount(req: ModifyAccountRequest): ModifyAccountResponse {
        ownerRepository.findById(req.ownerId)
            .orElseThrow(::RuntimeException)
        val account = accountRepository.findById(req.id)
            .orElseThrow(::RuntimeException)
        account.modifyAccountInfo(
            bank = req.bank,
            bankAccount = req.bankAccount,
            accountHolder = req.accountHolder
        )
        return ModifyAccountResponse(
            ownerId = account.owner.id,
            id = account.id,
            bank = account.bank,
            bankAccount = account.bankAccount,
            accountHolder = account.accountHolder
        )
    }
}


