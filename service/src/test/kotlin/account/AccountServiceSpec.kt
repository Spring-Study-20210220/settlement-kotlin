package settlement.kotlin.service.account

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import settlement.kotlin.db.owner.Account
import settlement.kotlin.db.owner.AccountRepository
import settlement.kotlin.db.owner.Owner
import settlement.kotlin.db.owner.OwnerRepository
import settlement.kotlin.service.owner.AccountService
import settlement.kotlin.service.owner.req.CreateAccountRequest
import java.util.*

class AccountServiceSpec : FeatureSpec() {
    private val accountRepository: AccountRepository = mockk()
    private val ownerRepository: OwnerRepository = mockk()

    private val accountService = AccountService(
        accountRepository = accountRepository,
        ownerRepository = ownerRepository
    )

    private val ownerIdSlot = slot<Long>()
    private val bankSlot = slot<String>()
    private val bankAccountSlot = slot<String>()
    val accountSlot = slot<Account>()

    private val testOwner = Owner(id = 1L, name = "testUser", email = "test@test", phoneNumber = "testNum")
    private val testAccount = Account(
        id = 1L,
        owner = testOwner,
        bank = "duplicatedBank",
        bankAccount = "duplicatedAccount",
        accountHolder = "testHolder"
    )

    init {
        feature("계좌 등록 기능") {
            beforeTest {
                every {
                    ownerRepository.findById(capture(ownerIdSlot))
                }.answers {
                    if (ownerIdSlot.captured == testOwner.id) Optional.of(testOwner)
                    else Optional.empty()
                }

                every {
                    accountRepository.findByOwnerIdAndBankAndBankAccount(
                        ownerId = capture(ownerIdSlot),
                        bank = capture(bankSlot),
                        bankAccount = capture(bankAccountSlot)
                    )
                }.answers {
                    if (ownerIdSlot.captured == testAccount.owner.id &&
                        bankSlot.captured == testAccount.bank &&
                        bankAccountSlot.captured == testAccount.bankAccount
                    ) testAccount
                    else null
                }

                every {
                    accountRepository.save(capture(accountSlot))
                }.answers {
                    val (id, owner, bank, bankAccount, accountHolder) = accountSlot.captured
                    Account(
                        id = id,
                        owner = owner,
                        bank = bank,
                        bankAccount = bankAccount,
                        accountHolder = accountHolder
                    )
                }
            }

            scenario("오너가 존재하지 않으면 예외가 발생한다.") {
                val req = CreateAccountRequest(
                    ownerId = 10,
                    bank = "testbank",
                    bankAccount = "testAccount",
                    accountHolder = "testUser"
                )

                shouldThrowExactly<RuntimeException> {
                    accountService.createAccount(req)
                }
            }

            scenario("오너 이름의 동일 계좌가 존재하면 예외가 발생한다.") {
                val req = CreateAccountRequest(
                    ownerId = 1L,
                    bank = "duplicatedBank",
                    bankAccount = "duplicatedAccount",
                    accountHolder = "testUser"
                )

                shouldThrowExactly<RuntimeException> {
                    accountService.createAccount(req)
                }
            }

            scenario("계좌가 정상적으로 등록된다.") {
                val req = CreateAccountRequest(
                    ownerId = 1L,
                    bank = "normalBank",
                    bankAccount = "normalBankAccount",
                    accountHolder = "testHolder"
                )

                val result = accountService.createAccount(req)

                result.ownerId shouldBe 1L
                result.bank shouldBe "normalBank"
                result.bankAccount shouldBe "normalBankAccount"
                result.accountHolder shouldBe "testHolder"
            }
        }

        feature("계좌 정보 수정 기능"){
            xscenario("정상적으로 계좌정보를 수정하였다."){

            }
        }
    }
}
